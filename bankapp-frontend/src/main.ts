// import { bootstrapApplication } from '@angular/platform-browser';
// import { AppComponent } from './app/app.component';
// import { provideHttpClient, withInterceptors } from '@angular/common/http';
// import { AuthInterceptor } from './app/auth.interceptor';

// bootstrapApplication(AppComponent, {
//   providers: [
//     provideHttpClient(
//       withInterceptors([
//         (req, next) => {
//           const token = localStorage.getItem('token');

//           if (token) {
//             const cloned = req.clone({
//               setHeaders: {
//                 Authorization: `Bearer ${token}`
//               }
//             });
//             return next(cloned);
//           }

//           return next(req);
//         }
//       ])
//     )
//   ]
// });
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app/app.routes';
import { provideRouter } from '@angular/router';
bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    provideRouter(routes)
  ]
}).catch(err => console.error(err));
