import { NgModule } from '@angular/core';
import { SharedModule } from "../shared/shared.module";

import { AddOrgComponent } from "./add-org/add-org.component";
import { RemoveOrgComponent } from "./remove-org/remove-org.component";
import { CreateChannelComponent } from "./create-channel/create-channel.component";

import { ChannelService } from "./channel.service";

@NgModule({
    imports: [SharedModule],
    exports: [],
    declarations: [AddOrgComponent, RemoveOrgComponent, CreateChannelComponent],
    providers: [ChannelService],
})
export class ChannelModule { }
