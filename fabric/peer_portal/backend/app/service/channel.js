'use strict';

const Service = require('egg').Service;
const shell = require('shelljs');

class ChannelService extends Service {

    generateDomain(orgName){
        return `${orgName}.com`;
    }

    get attributeccPath(){
        return this.config.fabric.chaincodePath.attributecc;
    }

    create({orgName, channelName}) {
        let domain = this.generateDomain(orgName)
        let chaincodePath = this.attributeccPath;
        return new Promise(function (reslove, reject) {
            let script = `source ./scripts/channel.sh;create ${orgName} ${channelName} ${domain} ${chaincodePath}`;
            shell.exec(script, {shell:'/bin/bash'}, function(code, stdout, stderr){
                if(code == 0){
                    reslove(stdout);
                }else{
                    reject(stderr);
                }
            });
        });
    }

    addOrg({adminOrgName, channelName, applicantOrgName}) {
        let adminOrgDomain = this.generateDomain(adminOrgName)
        let applicantOrgDomain = this.generateDomain(applicantOrgName);
        let chaincodePath = this.attributeccPath;


        return new Promise(function (reslove, reject) {
            let script = `source ./scripts/channel.sh;add_org ${adminOrgName} ${adminOrgDomain} ${channelName} ${applicantOrgName} ${applicantOrgDomain} ${chaincodePath}`;
            shell.exec(script, {shell:'/bin/bash'}, function(code, stdout, stderr){
                if(code == 0){
                    reslove(stdout);
                }else{
                    reject(stderr);
                }
            });
        });
    }

    removeOrg({adminOrgName, channelName, applicantOrgName}) {
        let adminOrgDomain = this.generateDomain(orgName)
        let chaincodePath = this.attributeccPath;
        return new Promise(function (reslove, reject) {
            let script = `source ./scripts/channel.sh;remove_org ${adminOrgName} ${adminOrgDomain} ${channelName} ${applicantOrgName} ${chaincodePath}`;
            shell.exec(script, {shell:'/bin/bash'}, function(code, stdout, stderr){
                if(code == 0){
                    reslove(stdout);
                }else{
                    reject(stderr);
                }
            });
        });
    }
}

module.exports = ChannelService;
