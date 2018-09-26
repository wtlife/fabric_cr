'use strict';

const Controller = require('egg').Controller;

class ChannelController extends Controller {
  async create() {
    try {
        let {orgName, channelName} = this.ctx.request.body;
        let result = await this.service.channel.create({orgName, channelName});
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

  async addOrganization(){
    try {
        let {adminOrgName, channelName, applicantOrgName} = this.ctx.request.body;
        let result = await this.service.channel.addOrg({adminOrgName, channelName, applicantOrgName});
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

  async removeOrganization(){
    try {
        let {adminOrgName, channelName, applicantOrgName} = this.ctx.request.body;
        let result = await this.service.channel.removeOrg({adminOrgName, channelName, applicantOrgName});
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

module.exports = ChannelController;
