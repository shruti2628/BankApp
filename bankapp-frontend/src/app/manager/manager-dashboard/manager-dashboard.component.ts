import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagerService } from '../manager.service';
import { Transaction } from '../../models/transaction.model';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.css']
})
export class ManagerDashboardComponent implements OnInit {

  transactions: Transaction[] = [];
  accounts: any[] = []
  account = {
  //accountNumber: '',
  holderName: '',
  //balance: 0,
  email: '',
  contactNumber: '',
  panNumber: ''
};

  constructor(private managerService: ManagerService) {}

  ngOnInit() {
    this.loadPending();
    this.loadAccounts();
  }

  loadPending() {
  this.managerService.getPending()
    .subscribe(data => {
      console.log("Pending transactions:", data);
      this.transactions = data;
    });
}

  loadAccounts() {
  this.managerService.getAllAccounts()
    .subscribe(data => this.accounts = data);
  }


  approve(id: number) {
    this.managerService.approve(id)
      .subscribe(() => this.loadPending());
  }

  reject(id: number) {
    this.managerService.reject(id)
      .subscribe(() => this.loadPending());
  }

//   createAccount() {
//   this.managerService.createAccount(this.account)
//     .subscribe(() => {
//       alert('Account created successfully');
//     });
// }
createdAccount: any = null;
createAccount() {
    this.managerService.createAccount(this.account)
      .subscribe({
        next: (res) => {
          this.createdAccount = res;
          alert('Account created successfully!');
          console.log(res);

          // Reset form
          this.account = {
            holderName: '',
            email: '',
            contactNumber: '',
            panNumber: ''
          };
        },
        error: (err) => {
          console.error(err);
          alert(err.error?.message || 'Error creating account');
        }
      });
      
  }
}
