'use strict';

const Service = require('egg').Service;
const Fabric_Client = require('fabric-client');
const path = require('path');
const fs = require('fs');
const _ = require('lodash');

class FabricClientService extends Service {

    constructor(ctx) {
        super(ctx);
    }

    get fabricClient(){
        return FabricClientService.FabricClient;
    }

    async setAdminContext() {
        let store_path = './hfc-key-store';
        let stateStore = await Fabric_Client.newDefaultKeyValueStore({path: store_path});
        this.fabricClient.setStateStore(stateStore);

        let crypto_suite = Fabric_Client.newCryptoSuite();
        let crypto_store = Fabric_Client.newCryptoKeyStore({
            path: store_path
        });
        crypto_suite.setCryptoKeyStore(crypto_store);
        this.fabricClient.setCryptoSuite(crypto_suite);

        let privatKeyFolderPath = path.join(this.config.crypto.mspFolderPath, this.config.crypto.privateKeyFolderName);
        let privateKeyFileName = _.first(fs.readdirSync(privatKeyFolderPath));
        let privateKey = path.join(privatKeyFolderPath, privateKeyFileName);
        let signedCertFolderPath = path.join(this.config.crypto.mspFolderPath, this.config.crypto.signedCertFolderName);
        let signedCertFileName = _.first(fs.readdirSync(signedCertFolderPath));
        let signedCert = path.join(signedCertFolderPath, signedCertFileName);

        let admin = await this.fabricClient.createUser({
            username: this.config.crypto.username,
            mspid: this.config.crypto.mspid,
            cryptoContent: {
                privateKey,
                signedCert
            }
        });
        let contextResult = await this.fabricClient.setUserContext(admin);
        return contextResult;
    }

    async query({channelId, ordererAddress, chaincodeId, fcn, args}){
        let channelConfig = this._getChannelConfig(channelId);
        let peer = this.fabricClient.newPeer(`grpc://${channelConfig.queryPeerAddress}`);
        const request = { chaincodeId, fcn, args, targets: [peer] };

        let channel = this._getChannel({channelId});
        if(!channel){
            channel = this._buildChannel({channelId, ordererAddress});
        }

        let results = await channel.queryByChaincode(request);
        var result =_.first(results);
        if (result instanceof Error) {
            throw new Error(result);
        }

        return JSON.parse(result.toString());
    }

    async invoke({channelId, ordererAddress, chaincodeId, fcn, args}){
        let channelConfig = this._getChannelConfig(channelId);
        let fabricClient = this.fabricClient;
        let targets = _.map(channelConfig.endorsedPeerAddresses,function(address){
            return fabricClient.newPeer(`grpc://${address}`);
        });

        let txId = this.fabricClient.newTransactionID(); 
        let proposalRequest = { chaincodeId, fcn, args, chainId: channelId, txId, targets };

        let channel = this._getChannel({channelId});
        if(!channel){
            channel = this._buildChannel({channelId, ordererAddress});
        }

        let proposalResult = await channel.sendTransactionProposal(proposalRequest);
        var proposalResponses = proposalResult[0];
        var proposal = proposalResult[1];

        this._checkProposalResponse(proposalResponses);

        var commitRequest = {
			proposalResponses: proposalResponses,
			proposal: proposal
        };

        var transaction_id_string = txId.getTransactionID();
		var promises = [];

		var sendPromise = channel.sendTransaction(commitRequest);
		promises.push(sendPromise); 

        let event_hub = this.fabricClient.newEventHub();
        event_hub.setPeerAddr(`grpc://${channelConfig.eventHubAddress}`);
        
        let txPromise = new Promise((resolve, reject) => {
			let handle = setTimeout(() => {
				event_hub.disconnect();
				resolve({
					event_status: 'TIMEOUT'
				});
			}, 60000);
			event_hub.connect();
			event_hub.registerTxEvent(transaction_id_string, (tx, code) => {
				clearTimeout(handle);
				event_hub.unregisterTxEvent(transaction_id_string);
				event_hub.disconnect();

				var return_status = {
					event_status: code,
					txId: transaction_id_string
				};
				if (code !== 'VALID') {
					console.error('The transaction was invalid, code = ' + code);
					resolve(return_status); 
				} else {
					console.log('The transaction has been committed on peer ' + event_hub._ep._endpoint.addr);
					resolve(return_status);
				}
			}, (err) => {
				reject(new Error('There was a problem with the eventhub ::' + err));
			});
        });
        promises.push(txPromise);

        let invokeResponses = await Promise.all(promises);

        this._checkInvokeResponse(invokeResponses);

        return invokeResponses[1].txId;
    }

    _getChannel({channelId}){
        let channel = this.fabricClient.getChannel(channelId, false);
        return channel;
    }

    _getChannelConfig(channelId){
        return this.config.fabric.channels[channelId];
    }

    _buildChannel({channelId}){
        let fabricClient = this.fabricClient; 
        let channel = fabricClient.newChannel(channelId);
        let channelConfig = this._getChannelConfig(channelId);
 
        _.forEach(channelConfig.ordererAddresses, function(address){
            var orderer = fabricClient.newOrderer(`grpc://${address}`);
            channel.addOrderer(orderer);
        });

        
        return channel;
    }

    _checkInvokeResponse(invokeResponses){
        console.log('Send transaction promise and event listener promise have completed');
        
        if (invokeResponses && invokeResponses[0] && invokeResponses[0].status !== 'SUCCESS') {
            throw new Error('Failed to order the transaction. Error code: ' + response.status);
        }
    
        if (invokeResponses && invokeResponses[1] && invokeResponses[1].event_status !== 'VALID') {
            throw new Error('Transaction failed to be committed to the ledger due to ::' + invokeResponses[1].event_status);
        }
    }

    _checkProposalResponse(proposalResponses){
        if (proposalResponses && proposalResponses[0].response &&
            proposalResponses[0].response.status !== 200) {
            throw new Error('Transaction proposal was bad');
        }
    }
}

FabricClientService.FabricClient = new Fabric_Client();

module.exports = FabricClientService;