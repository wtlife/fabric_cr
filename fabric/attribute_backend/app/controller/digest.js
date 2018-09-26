'use strict';

const Controller = require('egg').Controller;

class DigestController extends Controller {
    
  async appendDigestData() {
    try {
      this.ctx.validate({cts:"integer", sts:"integer", ahs:"string",dvhs:"string", dths:"string", s:"string" });
      
      let {cts, sts, ahs,dvhs, dths, s} = this.ctx.request.body;
      let txID = await this.service.digest.appendDigestData({cts, sts, ahs,dvhs, dths, s});
      this.ctx.body = {
        success: true,
        txID,
        message: "Successfully committed the data to the ledger by the peer"
      };
      
    } catch (error) {
      this.ctx.body = {
        success: false,
        message: error.errors || error.message || error
      }
    }
  }

  async queryDataByTimeRange() {
    try {
      this.ctx.validate({ sts:/^\d+$/, ets:/^\d+$/ }, this.ctx.request.query);

      let {sts, ets} = this.ctx.request.query;
      let result = await this.service.digest.queryDataByTimeRange({sts, ets});

      this.ctx.body = {
        success: true,
        result: result,
        message: "Query succussfully"
      };
      
    } catch (error) {
      this.ctx.body = {
        success: false,
        message: error.errors || error.message || error
      }
    }
  }
}

module.exports = DigestController;
