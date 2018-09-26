import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { ChannelService } from '../channel.service';

import { FabricNetworkService } from "../../core/fabric-network.service";
import { Organization } from '../../model/Organization';


@Component({
  selector: 'channel-create-channel',
  templateUrl: './create-channel.component.html',
  styleUrls: ['./create-channel.component.css']
})
export class CreateChannelComponent implements OnInit {

  form = new FormGroup({});
  model = { channelName: ''};
  fields: FormlyFieldConfig[] = [{
    key: 'channelName',
    type: 'input',
    templateOptions: {
      type: 'text',
      label: '名字',
      required: true,
    }
  }
];

 advertiser: Organization;

  constructor(private channelService:ChannelService, private fabricNetworkService: FabricNetworkService) { }

  ngOnInit() {
    this.advertiser = this.fabricNetworkService.getAdvertiser();
  }

  submit(model) {
    if (this.form.valid) {
      var orgName = this.advertiser.orgName;
      var channelName = model.channelName;

      this.channelService.createChannel({orgName, channelName}).subscribe(response=>{
        this.fabricNetworkService.addApplication({channelName, adminOrg: this.advertiser});
      });
    }
  }

}
