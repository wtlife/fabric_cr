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
      channelName: 'mychannel',
      chaincodeName: 'myattributecc',
    },
    digest: {
      channelName: 'mychannel',
      chaincodeName: 'mydigestcc',
    },
    channels: {
      "mychannel": {
        endorsedPeerAddresses: ['localhost:11051'],
        ordererAddresses: ['localhost:7050'],
        queryPeerAddress: 'localhost:14052',
        eventHubAddress: 'localhost:14054'
      }
    },
  };

  config.crypto = {
    mspFolderPath: '../workspaces/affiliate/crypto-config/peerOrganizations/org2.upltv.com/users/Admin@org2.upltv.com/msp',
    privateKeyFolderName: 'keystore',
    signedCertFolderName: 'signcerts',
    username: 'admin',
    mspid: 'org2MSP'
  };

  // add your config here
  config.middleware = [];

  return config;
};