package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	"fmt"
	"encoding/json"
	"bytes"
	"strconv"
)

type AttributeChaincode struct {
}

type attributeData struct {
	KindType        int64  `json:"kt"`
	ClientTimestamp int64  `json:"cts"`
	ServerTimestamp int64  `json:"sts"`
	APPHash         string `json:"ahs"`
	Data            string `json:"d"`
	DataHash        string `json:"dths"`
	Signature       string `json:"s"`
	DeviceHash      string `json:"dvhs,omitempty"`
	PlatformHash    string `json:"pths,omitempty"`
}

func main() {
	err := shim.Start(new(AttributeChaincode))
	if err != nil {
		fmt.Printf("Error starting install chaincode: %s", err)
	}
}

func (t *AttributeChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func (t *AttributeChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	fmt.Println("invoke is running " + function)

	switch function {
	case "appendData":
		if len(args) != 9 {
			return shim.Error("There must be 9 parameters")
		}

		kt, err := strconv.ParseInt(args[0], 10, 64)
		if err != nil {
			return shim.Error(args[0] + " is not a number")
		}

		cts, err := strconv.ParseInt(args[1], 10, 64)
		if err != nil {
			return shim.Error(args[1] + " is not a number")
		}

		sts, err := strconv.ParseInt(args[2], 10, 64)
		if err != nil {
			return shim.Error(args[2] + " is not a number")
		}

		data := &attributeData{kt, cts, sts, args[3], args[4], args[5], args[6], args[7], args[8]}

		return t.appendData(stub, data)
	case "queryDataByTimeRange":
		if len(args) != 3 {
			return shim.Error("there must be 3 parameters")
		}

		kt, err := strconv.ParseInt(args[0], 10, 64)
		if err != nil {
			return shim.Error(args[0] + " is not a number")
		}

		startTime, err := strconv.ParseInt(args[1], 10, 64)
		if err != nil {
			return shim.Error(args[1] + " is not a number")
		}

		endTime, err := strconv.ParseInt(args[2], 10, 64)
		if err != nil {
			return shim.Error(args[2] + " is not a number")
		}

		return t.queryDataByTimeRange(stub, kt, startTime, endTime)
	default:
		return shim.Error("Unsupported operation")
	}
}

func (t *AttributeChaincode) appendData(stub shim.ChaincodeStubInterface, data *attributeData) pb.Response {
	indexName := "kt~cts~sts~ahs~dths"

	indexKey, err := stub.CreateCompositeKey(indexName, []string{strconv.FormatInt(data.KindType, 10), strconv.FormatInt(data.ClientTimestamp, 10), strconv.FormatInt(data.ServerTimestamp, 10), data.APPHash, data.DataHash})
	fmt.Println(indexKey)

	if err != nil {
		return shim.Error(err.Error())
	}

	bytesValue, err := json.Marshal(data)
	if err != nil {
		return shim.Error(err.Error())
	}

	stub.PutState(indexKey, bytesValue)

	stub.SetEvent("appendData", []byte(indexKey))

	return shim.Success(nil)
}

func (t *AttributeChaincode) queryDataByTimeRange(stub shim.ChaincodeStubInterface, kindType int64, startTime int64, endTime int64) pb.Response {
	queryString := fmt.Sprintf("{\"selector\":{\"kt\":%d, \"sts\":{\"$gte\":%d, \"$lte\":%d}}}", kindType, startTime, endTime)

	queryResults, err := getQueryResultForQueryString(stub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}

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
