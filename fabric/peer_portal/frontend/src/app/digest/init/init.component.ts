import { Component, OnInit } from '@angular/core';
import { Organization } from '../../model/Organization';
import { OrganizationService } from '../../organization/organization.service';
import { FabricNetworkService } from '../../core/fabric-network.service';

@Component({
  selector: 'digestn-init',
  templateUrl: './init.component.html',
  styleUrls: ['./init.component.css']
})
export class InitComponent implements OnInit {

  constructor(private organizationService: OrganizationService, private fabricNetworkService: FabricNetworkService) { }

  ngOnInit() {

  }

  initDigest() {
    let organization: Organization = { orgName: 'digest', orgType: 'digest' };
    this.organizationService.createOrg(organization).subscribe(response => {
      this.fabricNetworkService.addOrg(organization);
    });
  }
}
