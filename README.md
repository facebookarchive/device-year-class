# ![Devices by Year Class](https://github.com/facebook/device-year-class/raw/master/docs/images/logo_trans_square.png) Device Year Class

Device Year Class is an Android library that implements a simple algorithm that maps
a device's RAM, CPU cores, and clock speed to the year where those combination
of specs were considered high end. This allows a developer to easily modify
application behavior based on the capabilities of the phone's hardware.

![Most Popular Devices by Year Class](https://github.com/facebook/device-year-class/raw/master/docs/images/popular_devices_by_year_class.png)

Mappings as of this writing (RAM is a ceiling):

| RAM | condition | Year Class |
|----:|----------:|-----------:|
|768MB| 1 core    | 2009 |
|     | 2+ cores  | 2010 |
|  1GB| <1.3GHz   | 2011 |
|     | 1.3GHz+   | 2012 |
|1.5GB| <1.8GHz   | 2012 |
|     | 1.8GHz+   | 2013 |
|  2GB|           | 2013 |
|  3GB|           | 2014 |
|  5GB|           | 2015 |
| more|           | 2016 |

## Integration

### Download
Download [the latest JARs](https://github.com/facebook/device-year-class/releases/latest) or grab via Gradle:
```groovy
compile 'com.facebook.device.yearclass:yearclass:2.1.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.facebook.device.yearclass</groupId>
  <artifactId>yearclass</artifactId>
  <version>2.1.0</version>
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
See the [CONTRIBUTING.md](https://github.com/facebook/device-year-class/blob/master/CONTRIBUTING.md) file for how to help out.

## License
Device Year Class is [BSD-licensed](https://github.com/facebook/device-year-class/blob/master/LICENSE). We also provide an additional patent grant.
