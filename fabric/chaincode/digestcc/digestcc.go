package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	"fmt"
	"encoding/json"
	"bytes"
	"strconv"
)

type DigestChaincode struct {
}

type digestData struct {
	ClientTimestamp int64  `json:"cts"`
	ServerTimestamp int64  `json:"sts"`
	APPHash         string `json:"ahs"`
	DeviceHash      string `json:"dvhs"`
	DataHash        string `json:"dths"`
	Signature       string `json:"s"`
}

func main() {
	err := shim.Start(new(DigestChaincode))
	if err != nil {
		fmt.Printf("Error starting install chaincode: %s", err)
	}
}

func (t *DigestChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func (t *DigestChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	fmt.Println("invoke is running " + function)

	switch function {
	case "appendDigestData":
		if len(args) != 6 {
			return shim.Error("there must be 6 parameters")
		}

		cts, err := strconv.ParseInt(args[0], 10, 64)
		if err != nil {
			return shim.Error(args[0] + " is not a number")
		}

		sts, err := strconv.ParseInt(args[1], 10, 64)
		if err != nil {
			return shim.Error(args[1] + " is not a number")
		}

		digestData := &digestData{cts, sts, args[2], args[3], args[4], args[5]}
		return t.appendDigestData(stub, digestData)
	case "queryDataByTimeRange":
	if len(args) != 2 {
			return shim.Error("there must be 2 parameters")
		}

		startTime, err := strconv.ParseInt(args[0], 10, 64)
		if err != nil {
			return shim.Error(args[0] + " is not a number")
		}

		endTime, err := strconv.ParseInt(args[1], 10, 64)
		if err != nil {
			return shim.Error(args[1] + " is not a number")
		}
		return t.queryDataByTimeRange(stub, startTime, endTime)
	default:
		return shim.Error("Unsupported operation")
	}
}

func (t *DigestChaincode) appendDigestData(stub shim.ChaincodeStubInterface, data *digestData) pb.Response {
	indexName := "cts~sts~ahs~dvhs"

	indexKey, err := stub.CreateCompositeKey(indexName, []string{strconv.FormatInt(data.ClientTimestamp, 10), strconv.FormatInt(data.ServerTimestamp, 10), data.APPHash, data.DeviceHash})
	if err != nil {
		return shim.Error(err.Error())
	}

	bytesValue, err := json.Marshal(data)
	if err != nil {
		return shim.Error(err.Error())
	}

	stub.PutState(indexKey, bytesValue)

	stub.SetEvent("appendDigestData", []byte(indexKey))

	return shim.Success(nil)
}

func (t *DigestChaincode) queryDataByTimeRange(stub shim.ChaincodeStubInterface, startTime int64, endTime int64) pb.Response {
	queryString := fmt.Sprintf("{\"selector\":{\"sts\":{\"$gte\":%d, \"$lte\":%d}}}", startTime, endTime)

	queryResults, err := getQueryResultForQueryString(stub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Println(queryResults)
	return shim.Success(queryResults)
}

func getQueryResultForQueryString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	fmt.Printf("- getQueryResultForQueryString queryString:\n%s\n", queryString)

	resultsIterator, err := stub.GetQueryResult(queryString)
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}

		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}

		buffer.WriteString(string(queryResponse.Value))

		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return buffer.Bytes(), nil
}