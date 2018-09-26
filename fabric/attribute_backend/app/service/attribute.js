const Service = require('egg').Service;
const shell = require('shelljs');

class AttributeService extends Service {

    get channelName(){
        return this.config.fabric.attribute.channelName;
    }

    get chaincodeName(){
        return this.config.fabric.attribute.chaincodeName;
    }

    appendData({kt, cts, sts, ahs, d, dths, s, dvhs='', pths=''}) {
        let channelId = this.channelName;
        let chaincodeId = this.chaincodeName;
        let args =[kt.toString(), cts.toString(), sts.toString(), ahs, d, dths, s, dvhs, pths];
        let fcn ="appendData";
        return this.service.fabricClient.invoke({channelId, chaincodeId, fcn, args});
   }

    queryDataByTimeRange({kt, sts, ets}) {
        let channelId = this.channelName;
        let chaincodeId = this.chaincodeName;
        let args =[kt.toString(), sts.toString(), ets.toString()];
        let fcn ="queryDataByTimeRange";

        return this.service.fabricClient.query({channelId, chaincodeId, fcn, args});
    }
}
module.exports = AttributeService;