'use strict';

const Controller = require('egg').Controller;

class OrganizationController extends Controller {
  async init() {
    try {
      let {orgName, orgType} = this.ctx.request.body;
      let result = await this.service.organization.init({orgName, orgType});
      this.ctx.body = {
        success: true,
        message: result
      };
    } catch (error) {
      this.ctx.body = {
        success: false,
        message: error.message || error
      }
    }
  }

  async deploy() {
    try {
      let {orgName, orgType} = this.ctx.request.body;
      let result = await this.service.organization.deploy({orgName, orgType});
      this.ctx.body = {
        success: true,
        message: result
      };
    } catch (error) {
      this.ctx.body = {
        success: false,
        message: error.message || error
      }
    }
  }

}

module.exports = OrganizationController;
