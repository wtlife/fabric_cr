'use strict';

module.exports = appInfo => {
  const config = exports = {};

  // use for cookie sign key, should change to your own and keep security
  config.keys = appInfo.name + '_1523952408676_2631';

  config.security = {
    csrf: false
  };

  config.fabric = {
    attribute: {
      channelName: 'appchannel',
      chaincodeName: 'appchannelattributecc',
    },
    digest: {
      channelName: 'digestchannel',
      chaincodeName: 'digestchanneldigestcc',
    },
    channels: {
      "appchannel": {
        endorsedPeerAddresses: ['localhost:7061'],
        ordererAddresses: ['localhost:7060'],
        queryPeerAddress: 'localhost:7071',
        eventHubAddress: 'localhost:7073'
      },
      "digestchannel": {
        endorsedPeerAddresses: ['localhost:7051'],
        ordererAddresses: ['localhost:7050'],
        queryPeerAddress: 'localhost:7071',
        eventHubAddress: 'localhost:7073'
      }
    },
  };
  
  config.crypto = {
    mspFolderPath: '../workspaces/affi1/crypto-config/peerOrganizations/affi1.com/users/Admin@affi1.com/msp',
    privateKeyFolderName: 'keystore',
    signedCertFolderName: 'signcerts',
    username: 'admin',
    mspid: 'affi1MSP'
  };

  // add your config here
  config.middleware = [];

  return config;
};