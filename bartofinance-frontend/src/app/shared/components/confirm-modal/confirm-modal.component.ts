import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.scss'
})
export class ConfirmModalComponent {
  @Input() show = false;
  @Input() title = 'Confirmar Ação';
  @Input() message = 'Tem certeza que deseja continuar?';
  @Input() confirmText = 'Confirmar';
  @Input() cancelText = 'Cancelar';
  @Input() type: 'danger' | 'warning' | 'info' = 'warning';
  @Input() icon = '⚠️';
  
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onConfirm(): void {
    this.confirm.emit();
  }

  onCancel(): void {
    this.cancel.emit();
  }

  getTypeClasses(): string {
    const classes = {
      danger: 'bg-red-50 border-red-200 text-red-800',
      warning: 'bg-yellow-50 border-yellow-200 text-yellow-800',
      info: 'bg-blue-50 border-blue-200 text-blue-800'
    };
    return classes[this.type];
  }

  getButtonClasses(): string {
    const classes = {
      danger: 'btn-primary bg-red-600 hover:bg-red-700 focus:ring-red-500',
      warning: 'btn-primary bg-yellow-600 hover:bg-yellow-700 focus:ring-yellow-500',
      info: 'btn-primary'
    };
    return classes[this.type];
  }
}

