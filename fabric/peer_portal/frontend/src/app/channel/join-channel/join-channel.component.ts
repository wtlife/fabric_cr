import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { ChannelService } from '../channel.service';

@Component({
  selector: 'channel-join-channel',
  templateUrl: './join-channel.component.html',
  styleUrls: ['./join-channel.component.css']
})
export class JoinChannelComponent implements OnInit {

  form = new FormGroup({});
  model = { channelName: '', orderAddress: '' };
  fields: FormlyFieldConfig[] = [{
    key: 'channelName',
    type: 'input',
    templateOptions: {
      type: 'text',
      label: 'Channel Name',
      placeholder: 'Enter channel name',
      required: true,
    }
  }, {
    key: 'orderAddress',
    type: 'input',
    templateOptions: {
      type: 'text',
      label: 'Order Address',
      placeholder: 'Enter order address',
      required: true,
    }
  }
  ];

  constructor(private channelService: ChannelService) { }

  ngOnInit() {
  }

  submit(model) {
    if (this.form.valid) {
      console.log(JSON.stringify(this.model));
    }
  }

}
