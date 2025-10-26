import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from './shared/components/toast/toast.component';
import { AiChatbotComponent } from './shared/components/ai-chatbot/ai-chatbot.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastComponent, AiChatbotComponent],
  template: `
    <router-outlet></router-outlet>
    <app-toast></app-toast>
    <app-ai-chatbot></app-ai-chatbot>
  `
})
export class AppComponent {
  title = 'BartoFinance';
}


