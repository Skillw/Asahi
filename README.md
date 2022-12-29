# Asahi
High performance function-based script language (with JSR223)

## Example

```
func add ( a b ) {
  calc '{&a} + {&b}'
}
print invoke func add with [ 1 2 ]
```

Result: `3.0`
