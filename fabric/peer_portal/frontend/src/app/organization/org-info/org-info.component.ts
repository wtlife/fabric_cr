import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '@ngx-formly/core';

@Component({
  selector: 'organization-org-info',
  templateUrl: './org-info.component.html',
  styleUrls: ['./org-info.component.css']
})
export class OrgInfoComponent implements OnInit {

  form = new FormGroup({});
  model = { organization: ''};
  fields: FormlyFieldConfig[] = [{
    key: 'orgnizationInfo',
    type: 'textarea',
    templateOptions: {
      rows: 20,
      label: 'Organization info',
    }
  }];

  constructor() { }

  ngOnInit() {
  }

  submit(model) {
    if (this.form.valid) {
      console.log(JSON.stringify(this.model));
    }
  }

}
