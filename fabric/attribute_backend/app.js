'use strict';

module.exports = app => {
  app.beforeStart(async () => {
    const ctx = app.createAnonymousContext();
    await ctx.service.fabricClient.setAdminContext();
  });
};
