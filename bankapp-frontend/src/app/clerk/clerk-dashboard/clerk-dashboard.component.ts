
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClerkService } from '../clerk.service';
import { Transaction } from '../../models/transaction.model';

@Component({
  selector: 'app-clerk-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clerk-dashboard.component.html',
  styleUrls: ['./clerk-dashboard.component.css']
})
export class ClerkDashboardComponent {

  accountNumber = '';
  amount = 0;
  message = '';

  transactions: Transaction[] = [];
  page = 0;
  size = 5;
  totalPages = 0;

  constructor(private clerkService: ClerkService) {}

  deposit() {
    this.clerkService.deposit(this.accountNumber, this.amount)
      .subscribe(() => {
        this.message = 'Amount deposited succesfully';
      });
  }

  withdraw() {
    if (this.amount <= 200000) {
    this.clerkService.withdraw(this.accountNumber, this.amount)
      .subscribe(() => {
        this.message = 'Amount withdrawn successfully';
      });
    } else {
      this.clerkService.withdraw(this.accountNumber, this.amount)
      .subscribe(() => {
        this.message = 'Withdraw request sent for manager approval';
      });
    }
  }

  loadTransactions() {
    if (!this.accountNumber) {
      this.message = 'Please enter account number first';
      return;
    }
    this.clerkService
      .getTransactions(this.accountNumber, this.page, this.size)
      .subscribe(response => {
        this.transactions = response.content;
        this.totalPages = response.totalPages;
      });
  }

  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadTransactions();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadTransactions();
    }
  }
}
