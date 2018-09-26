const Service = require('egg').Service;
const shell = require('shelljs');

class OrganizationService extends Service {

    getOuterPortof7051 (){
        var port = OrganizationService.OuterPort7051;
        OrganizationService.OuterPort7051 += 10;
        return port;
    }

    getOuterCouchDBPort (){
        var port = OrganizationService.OuterCouchDBPort;
        OrganizationService.OuterCouchDBPort += 10;
        return port;
    }

    getOuterPortof7050 (){
        var port = OrganizationService.OuterPort7050;
        OrganizationService.OuterPort7050 += 10;
        return port; 
    }

    generateDomain(orgName){
        return `${orgName}.com`;
    }

    init({orgName, orgType}) {
        let port_7051 = this.getOuterPortof7051();
        let couchDB_port = this.getOuterCouchDBPort();
        let port_7050 = this.getOuterPortof7050();

        let domain = this.generateDomain(orgName);

        return new Promise(function (reslove, reject) {
        let script = `source ./scripts/init.sh;init_${orgType} ${orgName} ${domain} ${port_7051} ${couchDB_port} ${port_7050}`;
            shell.exec(script, {shell:'/bin/bash'}, function(code, stdout, stderr){
                if(code == 0){
                    reslove(stdout);
                }else{
                    reject(stderr);
                }
            });
        });
    }

    deploy({orgName, orgType}) { 
        let domain = this.generateDomain(orgName);

        return new Promise(function (reslove, reject) {
            let script = `source ./scripts/deploy.sh;deploy_${orgType} ${orgName} ${domain}`;
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


OrganizationService.OuterPort7051 = 7051;
OrganizationService.OuterCouchDBPort = 5984;
OrganizationService.OuterPort7050 = 7050;

module.exports = OrganizationService;