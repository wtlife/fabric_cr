const Service = require('egg').Service;
const shell = require('shelljs');

class DigestService extends Service {

    get channelName(){
        return this.config.fabric.digest.channelName;
    }

    get chaincodeName(){
        return this.config.fabric.digest.chaincodeName;
    }

    appendDigestData({cts, sts, ahs,dvhs, dths, s}) {
        let channelId = this.channelName;
        let chaincodeId = this.chaincodeName;
        let args =[cts.toString(), sts.toString(), ahs,dvhs, dths, s];
        let fcn ="appendDigestData";
        return this.service.fabricClient.invoke({channelId, chaincodeId, fcn, args});
   }

    queryDataByTimeRange({sts, ets}) {
        let channelId = this.channelName
        let chaincodeId = this.chaincodeName;
        let args =[sts.toString(), ets.toString()];
        let fcn ="queryDataByTimeRange";

        return this.service.fabricClient.query({channelId, chaincodeId, fcn, args});
    }
}
module.exports = DigestService;