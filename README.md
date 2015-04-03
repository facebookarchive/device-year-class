# ![Devices by Year Class](https://github.com/facebook/device-year-class/raw/master/docs/images/logo_trans_square.png) Device Year Class

Device Year Class is an Android library that implements a simple algorithm that maps
a device's RAM, CPU cores, and clock speed to the year where those combination
of specs were considered high end. This allows a developer to easily modify
application behavior based on the capabilities of the phone's hardware.

![Most Popular Devices by Year Class](https://github.com/facebook/device-year-class/raw/master/docs/images/popular_devices_by_year_class.png)

Mappings as of this writing (ceilings, aside from the final row):

|Year|	Cores|	Clock |	RAM  |
|---:|------:|-------:|-----:|
|2008|	1    |	528MHz|	192MB|
|2009|	n/a  |	600MHz|	290MB|
|2010|	n/a  |	1.0GHz|	512MB|
|2011|	2    |	1.2GHz|	  1GB|
|2012|	4    |	1.5GHz|	1.5GB|
|2013|	n/a  |	2.0GHz|	  2GB|
|2014|	n/a  |   >2GHz|	 >2GB|


## Integration

### Download
Download [the latest JARs](https://github.com/facebook/device-year-class/releases/latest) or grab via Gradle:
```groovy
compile 'com.facebook.device.yearclass:yearclass:1.0.1'
```
or Maven:
```xml
<dependency>
  <groupId>com.facebook.device.yearclass</groupId>
  <artifactId>yearclass</artifactId>
  <version>1.0.1</version>
</dependency>
```

### Calculate Device Year Class
Calculating the current device's Year Class is simple.

```java
int year = YearClass.get(getApplicationContext());
```

Then, later on, you can use the year class to make decisions in your app, or
send it along with your analytics.

```java
if (year >= 2013) {
    // Do advanced animation
} else if (year > 2010) {
    // Do simple animation
} else {
    // Phone too slow, don't do any animations
}
```

See the `yearclass-sample` project for more details.

## Improve Device Year Class!
See the CONTRIBUTING.md file for how to help out.

## License
Device Year Class is BSD-licensed. We also provide an additional patent grant.
