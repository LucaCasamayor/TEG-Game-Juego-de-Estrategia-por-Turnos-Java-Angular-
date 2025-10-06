import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {FormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {CommonModule} from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';

@Component({
  selector: 'app-attack-troops-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './attack-troops-modal.component.html',
  styleUrl: './attack-troops-modal.component.css'
})
export class AttackTroopsModalComponent {
  troopsToAttack: number = 1;

  constructor(
    public dialogRef: MatDialogRef<AttackTroopsModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { maxTroops: number }
  ) {}

  confirm(): void {
    this.dialogRef.close(this.troopsToAttack);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
