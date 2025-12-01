# Cappuccino Options

**Cappuccino Options** is a robust expression language designed for high-precision arithmetic and strict type safety. It empowers developers to perform complex numerical calculations with absolute control over data types and accuracy, eliminating common pitfalls like floating-point errors and implicit casting surprises.

## Key Capabilities

### 🛡️ Strict Type Safety
Cappuccino Options prioritizes correctness. It does not allow implicit type coercion.
- **No Implicit Casting**: You cannot accidentally add an Integer to a Float. Operations must be between compatible types or explicitly handled.
- **Range Validation**: Every operation is checked against the target type's range. If a calculation overflows a `Byte` (-128 to 127), the system immediately reports a range error, preventing silent data corruption.

### 🎯 High Precision Arithmetic
Say goodbye to floating-point inaccuracies.
- **Decimal Precision**: All floating-point operations use arbitrary-precision arithmetic (similar to `BigDecimal`), ensuring that `0.1 + 0.2` equals exactly `0.3`.
- **Integer Precision**: Integer operations use arbitrary-precision integers (similar to `BigInteger`), allowing for calculations that exceed standard 64-bit limits.

## Supported Features

### 1. Numeric Types
The language supports a wide array of numeric types, each selectable via a case-insensitive suffix.

| Type | Suffix | Description | Example |
| :--- | :---: | :--- | :--- |
| **Byte** | `b` / `B` | 8-bit signed integer | `127b` |
| **Short** | `s` / `S` | 16-bit signed integer | `32000s` |
| **Integer** | `i` / `I` | 32-bit signed integer | `100i` |
| **Long** | `l` / `L` | 64-bit signed integer | `9000l` |
| **Float** | `f` / `F` | High-precision decimal | `3.14f` |
| **Double** | `d` / `D` | High-precision decimal | `10.5d` |
| **Number** | `n` / `N` | Generic number | `42n` |

### 2. Number Formats
Cappuccino Options understands that numbers come in many forms. It natively supports:
- **Decimal**: `123`, `12.34`
- **Hexadecimal**: `0xFF`, `0x1.A` (supports fractional hex)
- **Octal**: `0o755`
- **Binary**: `0b1011`
- **Scientific Notation**: `1.5e3`, `2.4E-5`

### 3. Advanced Type System
Beyond simple numbers, the language supports complex type structures for sophisticated data modeling:

- **Union Types**: Define a value that can be one of multiple types.
  ```typescript
  // A value that can be either an Integer or a Float
  let value: Integer | Float = 10i;
  ```

- **Tuple Types**: Group multiple values together with strict typing.
  ```typescript
  // A tuple containing an Integer and a Float
  let coordinate: [Integer, Float] = [10i, 20.5f];
  ```

### 4. Arithmetic Operations
Full support for standard mathematical operations with operator precedence:
- Addition (`+`)
- Subtraction (`-`)
- Multiplication (`*`)
- Division (`/`) - Precision-aware
- Modulus (`%`)
- Grouping with Parentheses `()`

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher.

### Running the Project
1. **Compile**:
   ```bash
   javac -d out -sourcepath . CappuccinoStarter.java
   ```

2. **Run**:
   ```bash
   java -cp out CappuccinoStarter
   ```

### Example Code
```java
// Define a scanner with an expression
CappuccinoScanner scanner = new CappuccinoScanner("0x1A + 5i");

// Parse and Interpret
CappuccinoParser parser = new CappuccinoParser(scanner);
CappuccinoInterpreter interpreter = new CappuccinoInterpreter(parser.parse());
interpreter.interpreter();
```
