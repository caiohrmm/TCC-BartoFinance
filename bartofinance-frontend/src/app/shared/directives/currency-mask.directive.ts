import { Directive, ElementRef, HostListener, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
  selector: '[appCurrencyMask]',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CurrencyMaskDirective),
      multi: true
    }
  ]
})
export class CurrencyMaskDirective implements ControlValueAccessor {
  private onChange: (value: number) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private el: ElementRef) {}

  @HostListener('input', ['$event'])
  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    
    if (value === '') {
      input.value = '';
      this.onChange(0);
      return;
    }

    // Converte para número (centavos)
    const numberValue = parseInt(value, 10) / 100;
    
    // Formata como moeda brasileira
    const formatted = new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(numberValue);

    input.value = formatted;
    this.onChange(numberValue);
  }

  @HostListener('blur')
  onBlur(): void {
    this.onTouched();
  }

  writeValue(value: number): void {
    if (value !== null && value !== undefined) {
      const formatted = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      }).format(value);
      this.el.nativeElement.value = formatted;
    } else {
      this.el.nativeElement.value = '';
    }
  }

  registerOnChange(fn: (value: number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.el.nativeElement.disabled = isDisabled;
  }
}

