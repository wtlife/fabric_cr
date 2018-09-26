import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { ChannelService } from "../channel.service";
import { Organization } from '../../model/Organization';
import { FabricNetworkService } from '../../core/fabric-network.service';
import { Application } from '../../model/Application';


@Component({
  selector: 'channel-add-org',
  templateUrl: './add-org.component.html',
  styleUrls: ['./add-org.component.css']
})
export class AddOrgComponent implements OnInit {

  form = new FormGroup({});
  model = { channelName: '', orgName: '' };
  fields: FormlyFieldConfig[] = [{
    key: 'channelName',
    type: 'select',
    templateOptions: {
      type: 'text',
      label: '应用名字',
      options: this.fabricNetworkService.getApplications(),
      valueProp: "channelName",
      labelProp: "channelName",
      required: true,
    }
  },
  {
    key: 'orgName',
    type: 'select',
    templateOptions: {
      type: 'text',
      label: '广告平台名称',
      options: this.fabricNetworkService.getAffilitates(),
      valueProp: "orgName",
      labelProp: "orgName", 
      required: true,
    }
  }];

  advertiser: Organization;

  constructor(private channelService: ChannelService, private fabricNetworkService: FabricNetworkService ) { }

  ngOnInit() {
   this.advertiser = this.fabricNetworkService.getAdvertiser();
  }

  submit(model) {
    var adminOrgName = this.advertiser.orgName;
    var channelName = model.channelName;
    var applicantOrgName = model.orgName;

    if (this.form.valid) {
      this.channelService.addOrg({adminOrgName, channelName, applicantOrgName}).subscribe(response=>{
        console.log(response);
      });
    }
  }

}
