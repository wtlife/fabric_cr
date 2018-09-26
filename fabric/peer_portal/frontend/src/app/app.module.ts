import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'

import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { CoreModule } from "./core/core.module";
import { SharedModule } from "./shared/shared.module";
import { OrganizationModule } from "./organization/organization.module";
import { ChannelModule } from "./channel/channel.module";
import { DigestModule } from "./digest/digest.module";
import { ReportModule } from "./report/report.module";

import { AppComponent } from './app.component';

import { JoinChannelComponent } from "./channel/join-channel/join-channel.component";
import { OrgInfoComponent } from "./organization/org-info/org-info.component";

import { PostbackConfigComponent } from './postback/config/config.component';
import { InvokeComponent } from './postback/invoke/invoke.component';
import { StatusComponent } from './tee/status/status.component';


@NgModule({
  declarations: [
    AppComponent,
    PostbackConfigComponent,
    InvokeComponent,
    StatusComponent,
    JoinChannelComponent,
    OrgInfoComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CoreModule,
    SharedModule,
    OrganizationModule,
    ChannelModule,
    DigestModule,
    ReportModule,
    AppRoutingModule,
    NgbModule.forRoot(),
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
