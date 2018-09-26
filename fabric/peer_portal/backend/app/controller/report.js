'use strict';

const Controller = require('egg').Controller;

class ReportController extends Controller {
  async summary() {
    try {
      let result = await this.service.report.query();
      this.ctx.body = {
        success: true,
        result
      };
    } catch (error) {
      this.ctx.body = {
        success: false,
        message: error.message || error
      };
    }
  }
}

module.exports = ReportController;