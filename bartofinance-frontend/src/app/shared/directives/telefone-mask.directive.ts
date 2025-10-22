import { Directive, ElementRef, HostListener, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
  selector: '[appTelefoneMask]',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TelefoneMaskDirective),
      multi: true
    }
  ]
})
export class TelefoneMaskDirective implements ControlValueAccessor {
  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private el: ElementRef) {}

  @HostListener('input', ['$event'])
  onInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    
    if (value.length > 11) {
      value = value.substring(0, 11);
    }

    // Salva apenas os números no FormControl (sem máscara)
    this.onChange(value);

    // Aplica a máscara (XX) XXXXX-XXXX ou (XX) XXXX-XXXX apenas visualmente
    let displayValue = value;
    if (value.length > 2) {
      displayValue = value.replace(/(\d{2})(\d)/, '($1) $2');
    }
    if (value.length > 6) {
      if (value.length <= 10) {
        displayValue = displayValue.replace(/(\(\d{2}\) \d{4})(\d)/, '$1-$2');
      } else {
        displayValue = displayValue.replace(/(\(\d{2}\) \d{5})(\d)/, '$1-$2');
      }
    }

    input.value = displayValue;
  }

  @HostListener('blur')
  onBlur(): void {
    this.onTouched();
  }

  writeValue(value: string): void {
    if (value) {
      const numericValue = value.replace(/\D/g, '');
      let displayValue = numericValue;
      
      if (numericValue.length > 2) {
        displayValue = numericValue.replace(/(\d{2})(\d)/, '($1) $2');
      }
      if (numericValue.length > 6) {
        if (numericValue.length <= 10) {
          displayValue = displayValue.replace(/(\(\d{2}\) \d{4})(\d)/, '$1-$2');
        } else {
          displayValue = displayValue.replace(/(\(\d{2}\) \d{5})(\d)/, '$1-$2');
        }
      }
      
      this.el.nativeElement.value = displayValue;
    } else {
      this.el.nativeElement.value = '';
    }
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.el.nativeElement.disabled = isDisabled;
  }
}

