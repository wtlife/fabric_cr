'use strict';

const Controller = require('egg').Controller;

//4=attribution, 3=impression, 2=click, 1=install, 0=IAP
const KIND_TYPES = [0, 1, 2, 3, 4];
const KIND_TYPES_STR = ["0", "1", "2", "3", "4"];

class AttributeController extends Controller {
    
  async appendData() {
    try {
      let {kt, cts, sts, ahs, d, dths, s, dvhs, pths} = this.ctx.request.body;
      this.ctx.validate({kt: KIND_TYPES, cts:"integer", sts:"integer",  ahs:"string",d:"string", dths:"string", s:"string"});

      switch (kt) {
        case 1:
          this.ctx.validate({dvhs: "string"});
          break;

        case 4:
          this.ctx.validate({pths: "string"});
          break;
      
        default:
          break;
      }
      
      let txID = await this.service.attribute.appendData({kt, cts, sts, ahs, d, dths, s, dvhs, pths});
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

      this.ctx.validate({ sts:/^\d+$/, ets:/^\d+$/, kt: KIND_TYPES_STR}, this.ctx.request.query);
      
      let {sts, ets, kt} = this.ctx.request.query;
      let result = await this.service.attribute.queryDataByTimeRange({kt, sts, ets});

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

module.exports = AttributeController;
