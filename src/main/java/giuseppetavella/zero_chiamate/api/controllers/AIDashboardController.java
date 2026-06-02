package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-dashboard")
public class AIDashboardController {

    /**
     * Send the HTML containing AI-generated dashboard.
     * 
     * @return
     */
    // @GetMapping
    // public ResponseEntity<String> sendAIGeneratedDashboard(@AuthenticationPrincipal User currentUser) {
    //    
    //     var html = """
    //            
    //            
    //             <h2 class="sr-only">SaaS metrics dashboard for Fictivo Inc., January through June 2025, showing MRR, churn, CAC, and new customers.</h2>
    //            
    //             <div style="padding: 1rem 0 0;">
    //               <p style="font-size: 13px; color: var(--color-text-secondary); margin: 0 0 1rem;">Fictivo Inc. — B2B SaaS · Jan–Jun 2025</p>
    //            
    //               <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 12px; margin-bottom: 1.5rem;">
    //                 <div style="background: var(--color-background-secondary); border-radius: var(--border-radius-md); padding: 1rem;">
    //                   <p style="font-size: 13px; color: var(--color-text-secondary); margin: 0 0 4px;">MRR (Jun)</p>
    //                   <p style="font-size: 22px; font-weight: 500; margin: 0;">$84,200</p>
    //                 </div>
    //                 <div style="background: var(--color-background-secondary); border-radius: var(--border-radius-md); padding: 1rem;">
    //                   <p style="font-size: 13px; color: var(--color-text-secondary); margin: 0 0 4px;">MRR growth</p>
    //                   <p style="font-size: 22px; font-weight: 500; margin: 0; color: #3B6D11;">+38%</p>
    //                 </div>
    //                 <div style="background: var(--color-background-secondary); border-radius: var(--border-radius-md); padding: 1rem;">
    //                   <p style="font-size: 13px; color: var(--color-text-secondary); margin: 0 0 4px;">Avg churn</p>
    //                   <p style="font-size: 22px; font-weight: 500; margin: 0; color: #A32D2D;">3.1%</p>
    //                 </div>
    //                 <div style="background: var(--color-background-secondary); border-radius: var(--border-radius-md); padding: 1rem;">
    //                   <p style="font-size: 13px; color: var(--color-text-secondary); margin: 0 0 4px;">CAC (Jun)</p>
    //                   <p style="font-size: 22px; font-weight: 500; margin: 0;">$310</p>
    //                 </div>
    //               </div>
    //            
    //               <div style="display: flex; flex-wrap: wrap; gap: 16px; margin-bottom: 8px; font-size: 12px; color: var(--color-text-secondary);">
    //                 <span style="display: flex; align-items: center; gap: 4px;"><span style="width: 10px; height: 10px; border-radius: 2px; background: #185FA5;"></span>MRR ($)</span>
    //                 <span style="display: flex; align-items: center; gap: 4px;"><span style="width: 10px; height: 10px; border-radius: 2px; background: #639922;"></span>New customers</span>
    //               </div>
    //               <div style="position: relative; width: 100%; height: 220px; margin-bottom: 1.5rem;">
    //                 <canvas id="mrrChart" role="img" aria-label="Line and bar chart showing MRR growing from $61k in January to $84k in June, and new customers growing from 38 to 67.">MRR: Jan $61k, Feb $65k, Mar $68k, Apr $72k, May $78k, Jun $84k. New customers: 38, 41, 45, 52, 60, 67.</canvas>
    //               </div>
    //            
    //               <div style="display: flex; flex-wrap: wrap; gap: 16px; margin-bottom: 8px; font-size: 12px; color: var(--color-text-secondary);">
    //                 <span style="display: flex; align-items: center; gap: 4px;"><span style="width: 10px; height: 10px; border-radius: 2px; background: #E24B4A;"></span>Churn rate (%)</span>
    //                 <span style="display: flex; align-items: center; gap: 4px;"><span style="width: 10px; height: 10px; border-radius: 2px; background: #BA7517; border: 1px dashed #BA7517;"></span>CAC ($)</span>
    //               </div>
    //               <div style="position: relative; width: 100%; height: 200px; margin-bottom: 1.5rem;">
    //                 <canvas id="churnCacChart" role="img" aria-label="Dual axis chart showing churn rate peaking at 3.8% in March then declining, while CAC drops from $390 in January to $310 in June.">Churn: Jan 2.8%, Feb 3.2%, Mar 3.8%, Apr 3.1%, May 2.9%, Jun 2.6%. CAC: Jan $390, Feb $360, Mar $340, Apr $330, May $320, Jun $310.</canvas>
    //               </div>
    //            
    //               <div style="border-top: 0.5px solid var(--color-border-tertiary); padding-top: 1rem;">
    //                 <p style="font-size: 13px; color: var(--color-text-secondary); margin: 0 0 8px;">Revenue by plan — Jun 2025</p>
    //                 <div style="display: flex; flex-wrap: wrap; gap: 16px; margin-bottom: 8px; font-size: 12px; color: var(--color-text-secondary);">
    //                   <span style="display: flex; align-items: center; gap: 4px;"><span style="width: 10px; height: 10px; border-radius: 2px; background: #185FA5;"></span>Starter 22%</span>
    //                   <span style="display: flex; align-items: center; gap: 4px;"><span style="width: 10px; height: 10px; border-radius: 2px; background: #3B6D11;"></span>Growth 45%</span>
    //                   <span style="display: flex; align-items: center; gap: 4px;"><span style="width: 10px; height: 10px; border-radius: 2px; background: #534AB7;"></span>Enterprise 33%</span>
    //                 </div>
    //                 <div style="position: relative; width: 100%; height: 180px;">
    //                   <canvas id="planChart" role="img" aria-label="Donut chart showing revenue by plan: Starter 22%, Growth 45%, Enterprise 33%.">Starter 22%, Growth 45%, Enterprise 33%.</canvas>
    //                 </div>
    //               </div>
    //             </div>
    //            
    //             <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js"></script>
    //             <script>
    //             const months = ['Jan','Feb','Mar','Apr','May','Jun'];
    //             const mrr = [61000,65400,68200,72100,78300,84200];
    //             const newCust = [38,41,45,52,60,67];
    //             const churn = [2.8,3.2,3.8,3.1,2.9,2.6];
    //             const cac = [390,360,340,330,320,310];
    //            
    //             new Chart(document.getElementById('mrrChart'), {
    //               data: {
    //                 labels: months,
    //                 datasets: [
    //                   { type:'line', label:'MRR', data: mrr, borderColor:'#185FA5', backgroundColor:'rgba(24,95,165,0.08)', tension:0.4, yAxisID:'y', pointRadius:3 },
    //                   { type:'bar', label:'New customers', data: newCust, backgroundColor:'rgba(99,153,34,0.7)', yAxisID:'y2' }
    //                 ]
    //               },
    //               options: {
    //                 responsive: true, maintainAspectRatio: false,
    //                 plugins: { legend: { display: false } },
    //                 scales: {
    //                   y: { position:'left', ticks: { callback: v => '$'+(v/1000).toFixed(0)+'k', font:{size:11} }, grid:{color:'rgba(128,128,128,0.1)'} },
    //                   y2: { position:'right', ticks: { font:{size:11} }, grid:{drawOnChartArea:false} },
    //                   x: { ticks: { font:{size:11} }, grid:{color:'rgba(128,128,128,0.1)'} }
    //                 }
    //               }
    //             });
    //            
    //             new Chart(document.getElementById('churnCacChart'), {
    //               data: {
    //                 labels: months,
    //                 datasets: [
    //                   { type:'line', label:'Churn %', data: churn, borderColor:'#E24B4A', tension:0.4, yAxisID:'y', pointRadius:3 },
    //                   { type:'line', label:'CAC', data: cac, borderColor:'#BA7517', borderDash:[5,4], tension:0.4, yAxisID:'y2', pointRadius:3 }
    //                 ]
    //               },
    //               options: {
    //                 responsive: true, maintainAspectRatio: false,
    //                 plugins: { legend: { display: false } },
    //                 scales: {
    //                   y: { position:'left', ticks: { callback: v => v+'%', font:{size:11} }, grid:{color:'rgba(128,128,128,0.1)'} },
    //                   y2: { position:'right', ticks: { callback: v => '$'+v, font:{size:11} }, grid:{drawOnChartArea:false} },
    //                   x: { ticks: { font:{size:11} }, grid:{color:'rgba(128,128,128,0.1)'} }
    //                 }
    //               }
    //             });
    //            
    //             new Chart(document.getElementById('planChart'), {
    //               type: 'doughnut',
    //               data: {
    //                 labels: ['Starter','Growth','Enterprise'],
    //                 datasets: [{ data:[22,45,33], backgroundColor:['#185FA5','#639922','#534AB7'], borderWidth:0 }]
    //               },
    //               options: {
    //                 responsive: true, maintainAspectRatio: false,
    //                 plugins: { legend: { display: false } }
    //               }
    //             });
    //             </script>
    //            
    //     """;
    //    
    //     return ResponseEntity.ok()
    //             .contentType(MediaType.TEXT_HTML)
    //             .header("X-Frame-Options", "SAMEORIGIN")           // only YOUR site can embed it
    //             .header("Content-Security-Policy",
    //                     "default-src 'self'; " +
    //                             "script-src 'self' https://cdnjs.cloudflare.com; " + // whitelist known CDNs
    //                             "style-src 'self' 'unsafe-inline'; " +
    //                             "frame-ancestors 'self'")                       // redundant with X-Frame-Options, belt+suspenders
    //             .header("X-Content-Type-Options", "nosniff")
    //             .body(html);
    // }

}
