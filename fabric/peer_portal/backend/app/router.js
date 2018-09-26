'use strict';

/**
 * @param {Egg.Application} app - egg application
 */
module.exports = app => {
  const { router, controller } = app;
  router.post('/organization/init', controller.organization.init);
  router.post('/organization/deploy', controller.organization.deploy);

  router.post('/channel/create', controller.channel.create);
  router.post('/channel/addorg', controller.channel.addOrganization);
  router.post('/channel/removeorg', controller.channel.addOrganization);
  
  router.get('/report/summary', controller.report.summary)
};
