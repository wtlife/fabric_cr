import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AddOrgComponent } from "./channel/add-org/add-org.component";
import { RemoveOrgComponent } from "./channel/remove-org/remove-org.component";
import { CreateChannelComponent } from "./channel/create-channel/create-channel.component";

import { InitComponent } from "./digest/init/init.component";

// import { OrgInfoComponent } from "./organization/org-info/org-info.component";
import { CreateOrgComponent } from "./organization/create-org/create-org.component";

import { SummaryComponent } from "./report/summary/summary.component";


const routes: Routes = [
  { path: '', redirectTo: '/initdigest', pathMatch: 'full' },

  { path: 'addorg', component: AddOrgComponent },
  { path: 'removeorg', component: RemoveOrgComponent },
  { path: 'createchannel', component: CreateChannelComponent },

  { path: 'initdigest', component: InitComponent },
  { path: 'createadv', component: CreateOrgComponent },
  { path: 'createaffi', component: CreateOrgComponent },

  { path: 'summaryreport', component: SummaryComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
