import { Injectable } from '@angular/core';
import { Organization } from "../model/Organization";
import * as _ from "lodash";
import { Application } from '../model/Application';

@Injectable()
export class FabricNetworkService {

  private orgs: Organization[] = [];

  private applications: Application[] = [];

  public addOrg(org: Organization){
    this.orgs.push(org);
  }

  public getAffilitates(){
    return _.filter(this.orgs, {orgType: "affi"});
  }

  public getAdvertiser(): Organization{
    let advertiser = _.find(this.orgs, {orgType: "adv"});
    return Object.assign({}, advertiser);
  }

  public getApplications(): Application[]{
    return this.applications;
  }

  public addApplication(application: Application){
    this.applications.push(application);
  }

}
