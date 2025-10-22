import { Directive, ElementRef, HostListener, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
  selector: '[appCpfMask]',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CpfMaskDirective),
      multi: true
    }
  ]
})
export class CpfMaskDirective implements ControlValueAccessor {
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

    // Aplica a máscara XXX.XXX.XXX-XX apenas visualmente
    let displayValue = value;
    if (value.length > 3) {
      displayValue = value.replace(/(\d{3})(\d)/, '$1.$2');
    }
    if (value.length > 6) {
      displayValue = displayValue.replace(/(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
    }
    if (value.length > 9) {
      displayValue = displayValue.replace(/(\d{3})\.(\d{3})\.(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
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
      
      if (numericValue.length > 3) {
        displayValue = numericValue.replace(/(\d{3})(\d)/, '$1.$2');
      }
      if (numericValue.length > 6) {
        displayValue = displayValue.replace(/(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
      }
      if (numericValue.length > 9) {
        displayValue = displayValue.replace(/(\d{3})\.(\d{3})\.(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
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

