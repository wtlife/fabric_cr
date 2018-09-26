import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '@ngx-formly/core';

@Component({
  selector: 'channel-remove-org',
  templateUrl: './remove-org.component.html',
  styleUrls: ['./remove-org.component.css']
})
export class RemoveOrgComponent implements OnInit {
  form = new FormGroup({});
  model = { channelName: '', orgName: '' };
  fields: FormlyFieldConfig[] = [{
    key: 'channelName',
    type: 'input',
    templateOptions: {
      type: 'text',
      label: 'Channel Name',
      placeholder: 'Enter channel name',
      required: true,
    }
  },
  {
    key: 'orgName',
    type: 'input',
    templateOptions: {
      type: 'text',
      label: 'Organization Name',
      placeholder: 'Enter organization name',
      required: true,
    }
  }];

  constructor() { }

  ngOnInit() {
  }

  submit(model) {
    if (this.form.valid) {
      alert(JSON.stringify(this.model));
    }
  }

}
