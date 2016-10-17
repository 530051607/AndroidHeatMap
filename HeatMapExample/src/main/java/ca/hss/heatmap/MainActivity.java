/**
 * MainActivity.java
 *
 * Copyright 2016 Heartland Software Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the license at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LIcense is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.hss.heatmap;

import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Map;
import java.util.Random;

import ca.hss.heatmaplib.HeatMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HeatMap map = (HeatMap)findViewById(R.id.example_map);
        map.setMinimum(0.0);
        map.setMaximum(100.0);
        Map<Float, Integer> colors = new ArrayMap<>();
        //build a color gradient in HSV from red at the center to green at the outside
        for (int i = 0; i < 21; i++) {
            float stop = ((float)i) / 20.0f;
            int color = doGradient(i * 5, 0, 100, 0xff00ff00, 0xffff0000);
            colors.put(stop, color);
        }
        map.setColorStops(colors);
        Random rand = new Random();
        //add 20 random points of random intensity
        for (int i = 0; i < 20; i++) {
            HeatMap.DataPoint point = new HeatMap.DataPoint(clamp(rand.nextFloat(), 0.0f, 1.0f),
                    clamp(rand.nextFloat(), 0.0f, 1.0f), clamp(rand.nextDouble(), 0.0, 100.0));
            map.addData(point);
        }
    }

    private float clamp(float value, float min, float max) {
        return value * (max - min) + min;
    }

    private double clamp(double value, double min, double max) {
        return value * (max - min) + min;
    }

    private static int doGradient(double value, double min, double max, int min_color, int max_color) {
        if (value >= max) {
            return max_color;
        }
        if (value <= min) {
            return min_color;
        }
        float[] hsvmin = new float[3];
        float[] hsvmax = new float[3];
        float frac = (float)((value - min) / (max - min));
        Color.RGBToHSV(Color.red(min_color), Color.green(min_color), Color.blue(min_color), hsvmin);
        Color.RGBToHSV(Color.red(max_color), Color.green(max_color), Color.blue(max_color), hsvmax);
        float[] retval = new float[3];
        for (int i = 0; i < 3; i++) {
            retval[i] = interpolate(hsvmin[i], hsvmax[i], frac);
        }
        return Color.HSVToColor(retval);
    }

    private static float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
    }
}
