/* This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.facebook.device.yearclass.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import com.facebook.device.yearclass.YearClass;

public class MainActivity extends Activity {
  private static final String PREF_FILE = "YearClass";
  private static final String PREF_NAME = "yearclass";
  private TextView mYearClass;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
              .detectAll()
              .penaltyLog()
              .build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
              .detectAll()
              .penaltyLog()
              .penaltyDeath()
              .build());
    }

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GetOrComputeYearClass findYearClass = new GetOrComputeYearClass();
    findYearClass.execute();
    mYearClass = (TextView) findViewById(R.id.year_class);
  }

  private class GetOrComputeYearClass extends AsyncTask<Void, Void, Integer> {

    @Override
    protected Integer doInBackground(Void... voids) {
      int yearClass = YearClass.CLASS_UNKNOWN;
      SharedPreferences prefs = getSharedPreferences(PREF_FILE, 0);
      if (prefs.contains(PREF_NAME)) {
        yearClass = prefs.getInt(PREF_NAME, YearClass.CLASS_UNKNOWN);
      }
      //Try again if device was previously unknown.
      if (yearClass == YearClass.CLASS_UNKNOWN) {
        yearClass = YearClass.get(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_NAME, yearClass);
        editor.apply();
      }
      return yearClass;
    }

    @Override
    protected void onPostExecute(Integer yearClass) {
      //update UI
      mYearClass.setText(Integer.toString(yearClass));
    }
  }
}
