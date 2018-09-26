import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageListComponent } from "./message-list/message-list.component"
import { MessageService } from "./message.service";
import { RequestService } from "./request.service";
import { FabricNetworkService } from './fabric-network.service';

@NgModule({
  imports: [ CommonModule ],
  exports: [ MessageListComponent  ],
  declarations: [ MessageListComponent ],
  providers: [ MessageService, RequestService, FabricNetworkService ]
})
export class CoreModule { };