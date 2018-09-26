'use strict';

const Service = require('egg').Service;

class ReportService extends Service {

    get knex(){
        return ReportService.knex;
    }

    initConnection(){
        let knex = require('knex')({
            client: 'pg',
            connection: this.config.tee.dbconnection,
            pool: { min: 0, max: 7 }
          });

        ReportService.knex = knex;
    }

    query(){
        let queryResult = this.knex.select().table('summary_report');
        return queryResult;
    }
}



module.exports = ReportService;



