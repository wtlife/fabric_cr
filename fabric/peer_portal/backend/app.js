'use strict';

module.exports = app => {
  app.beforeStart(async () => {
    const ctx = app.createAnonymousContext();
    ctx.service.report.initConnection();
  });
};
