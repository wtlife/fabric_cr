'use strict';

module.exports = appInfo => {
  const config = exports = {};

  config.security = {
    csrf: false
  };

  config.fabric = {
    attribute: {
      channelName: 'chnattribute',
      chaincodeName: 'attributecc',
    },
    digest: {
      channelName: 'chndigest',
      chaincodeName: 'digestcc',
    },
    channels: {
      "chnattribute": {
        endorsedPeerAddresses: ['172.24.10.78:7051'],
        ordererAddresses: ['172.24.10.78:7050'],
        queryPeerAddress: 'localhost:7051',
        eventHubAddress: 'localhost:7053'
      },
      "chndigest": {
        endorsedPeerAddresses: ['172.24.20.171:7051'],
        ordererAddresses: ['172.24.20.171:7050'],
        queryPeerAddress: 'localhost:7051',
        eventHubAddress: 'localhost:7053'
      }
    },
  };

  config.crypto = {
    mspFolderPath: '../workspaces/google/crypto-config/peerOrganizations/google.com/users/Admin@google.com/msp',
    privateKeyFolderName: 'keystore',
    signedCertFolderName: 'signcerts',
    username: 'admin',
    mspid: 'googleMSP'
  };

  return config;
};