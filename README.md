# Device Year Class

Device Year Class is a library that implements a simple algorithm that maps 
a device's RAM, CPU cores, and clock speed to the year where those combination 
of specs were considered high end. This allows a developer to easily modify 
application behavior based on the capabilities of the phone's hardware.

## Integration

### Download
Download [the latest JARs](https://github.com/facebook/yearclass/releases/latest) or grab via Gradle:
```groovy
compile 'com.facebook.device.yearclass:yearclass:1.0.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.facebook.device.yearclass</groupId>
  <artifactId>yearclass</artifactId>
  <version>1.0.0</version>
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