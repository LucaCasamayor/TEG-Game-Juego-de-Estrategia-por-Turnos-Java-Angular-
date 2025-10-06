import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-fortify-troops-modal',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './fortify-troops-modal.component.html',
  styleUrl: './fortify-troops-modal.component.css'
})
export class FortifyTroopsModalComponent {
  troopsToFortify: number = 1;

  constructor(
    public dialogRef: MatDialogRef<FortifyTroopsModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { maxTroops: number }
  ) {}

  confirm(): void {
    this.dialogRef.close(this.troopsToFortify);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
