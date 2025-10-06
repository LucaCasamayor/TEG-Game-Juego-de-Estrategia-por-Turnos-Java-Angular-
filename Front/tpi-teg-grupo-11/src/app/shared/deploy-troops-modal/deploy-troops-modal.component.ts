import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-deploy-troops-modal',
  standalone: true,
  templateUrl: './deploy-troops-modal.component.html',
  styleUrls: ['./deploy-troops-modal.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class DeployTroopsModalComponent {
  troopsToDeploy: number = 1;

  constructor(
    public dialogRef: MatDialogRef<DeployTroopsModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { maxTroops: number }
  ) {}

  confirm(): void {
    this.dialogRef.close(this.troopsToDeploy);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
