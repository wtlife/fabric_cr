'use strict';

/**
 * @param {Egg.Application} app - egg application
 */
module.exports = app => {
  const { router, controller } = app;
  router.post('/attribute/appendData', controller.attribute.appendData);
  router.get('/attribute/queryDataByTimeRange', controller.attribute.queryDataByTimeRange);

  router.post('/digest/appendDigestData', controller.digest.appendDigestData);
  router.get('/digest/queryDataByTimeRange', controller.digest.queryDataByTimeRange);
};
