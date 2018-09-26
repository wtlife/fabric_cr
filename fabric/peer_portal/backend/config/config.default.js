'use strict';

module.exports = appInfo => {
  const config = exports = {};

  config.static = {
    prefix: ''
  };

  config.security = {
    csrf: {
      enable: false
    },
    domainWhiteList: [ 'http://localhost:4200' ],
  };

  config.cors = {
    origin: '*',
    allowMethods: 'POST,OPTIONS'
  };

  config.cluster = {
    listen: {
      port: 8001
    }
  };

  config.fabric = {
    chaincodePath:{
      attributecc: "attributecc"
    }
  };


  // add your config here
  config.middleware = [];

  config.tee = {
    dbconnection:{
      host: '13.228.172.185',
      user: 'gpadmin',
      password: 'pivotal',
      database: 'gpadmin',
      pool: { min: 2, max: 10 }
    }
  };

  return config;
};
