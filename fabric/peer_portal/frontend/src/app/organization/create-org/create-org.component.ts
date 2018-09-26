import { Component, OnInit, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { ActivatedRoute } from "@angular/router";
import { OrganizationService } from "../organization.service";
import { FabricNetworkService } from "../../core/fabric-network.service";
import { Organization } from '../../model/Organization';


@Component({
  selector: 'app-create-org',
  templateUrl: './create-org.component.html',
  styleUrls: ['./create-org.component.css']
})
export class CreateOrgComponent implements OnInit {
  orgType: string;

  form = new FormGroup({});
  model = { orgName: '' };
  fields: FormlyFieldConfig[] = [{
    key: 'orgName',
    type: 'input',
    templateOptions: {
      type: 'text',
      label: '名字',
      required: true,
    }
  }
  ];

  constructor(private route: ActivatedRoute, private organizationService: OrganizationService, private fabricNetworkService: FabricNetworkService) {

  }

  ngOnInit() {
    this.orgType = this.route.snapshot.url[0].path.substr(6);
  }

  submit(model) {
    if (this.form.valid) {
      let organization: Organization = {orgName: model.orgName, orgType: this.orgType};
      this.organizationService.createOrg(organization).subscribe(response => {
        this.fabricNetworkService.addOrg(organization); 
      },(error)=>{
        alert(error);
      });
    }
  }
}