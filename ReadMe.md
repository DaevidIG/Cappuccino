# Cappuccino Options

**Cappuccino Options** is a custom expression language interpreter written in Java. It supports various numeric types, arithmetic operations, and strict type checking, allowing for precise control over numerical calculations.

## Features

### 1. Arithmetic Operations
Cappuccino supports standard arithmetic operations:
- **Addition (`+`)**
- **Subtraction (`-`)**
- **Multiplication (`*`)**
- **Division (`/`)**: Performs integer division for integers and precision division for decimals.
- **Modulus (`%`)**
- **Grouping**: Parentheses `()` can be used to control the order of operations.

### 2. Numeric Types & Literals
The language supports multiple numeric types, each identified by a specific suffix (case-insensitive):

| Type | Suffix | Example | Description |
| :--- | :---: | :--- | :--- |
| **Byte** | `b` or `B` | `125b` | 8-bit signed integer |
| **Short** | `s` or `S` | `32000s` | 16-bit signed integer |
| **Integer** | `i` or `I` | `100i` | 32-bit signed integer |
| **Long** | `l` or `L` | `5000l` | 64-bit signed integer |
| **Float** | `f` or `F` | `3.14f` | 32-bit floating point |
| **Double** | `d` or `D` | `10.5d` | 64-bit floating point |
| **Number** | `n` or `N` | `42n` | Generic number type |

*Note: If no suffix is provided, the interpreter treats the number as a generic `DigitLiteralToken`.*

### 3. Type Safety & Mixing Rules
Cappuccino enforces strict rules when mixing different numeric types in operations to prevent unintended precision loss or overflow.

- **Decimal Operations**: If both operands are `BigDecimal` (e.g., Float, Double), the result is calculated with high precision.
- **Integer Operations**: If both operands are `BigInteger` (e.g., Byte, Short, Integer, Long), the result is an integer.
- **Mixed Operations**: Mixing "Decimal" types with "Integer" types (e.g., adding a `Float` to an `Integer`) will result in an error:
  > `Cannot evaluate an decimal operation with an integer operation`

- **Homogeneous Type Checks**: The interpreter checks if you are mixing different specific types (e.g., adding a `Byte` to a `Short`) and may report errors like:
  - `Byte mix`
  - `Short mix`
  - `Integer mix`
  - `Float mix`
  - `Double mix`

### 4. Range Checking
The interpreter validates that literals fit within their respective type ranges (e.g., a `Byte` must be between -128 and 127). If a value exceeds its range, an error is reported (e.g., `Byte range`).

## Installation & Usage

### Prerequisites
- Java Development Kit (JDK) 17 or higher.

### Running the Project
The entry point is `CappuccinoStarter.java`. You can run it using your IDE or from the command line.

#### Command Line
1. **Navigate to the project root directory**:
   ```bash
   cd "Cappuccino Options"
   ```

2. **Compile the code**:
   ```bash
   javac -d out -sourcepath . CappuccinoStarter.java
   ```

3. **Run the interpreter**:
   ```bash
   java -cp out CappuccinoStarter
   ```

### Example Usage
In `CappuccinoStarter.java`, you can define an expression to evaluate:

```java
CappuccinoScanner cappuccinoScanner = new CappuccinoScanner("-125b + -3b");
CappuccinoParser cappuccinoParser = new CappuccinoParser(cappuccinoScanner);
CappuccinoProgramNode cappuccinoNode = cappuccinoParser.parse();
CappuccinoInterpreter cappuccinoInterpreter = new CappuccinoInterpreter(cappuccinoNode);
cappuccinoInterpreter.interpreter();
```

**Output:**
```
-128
Time in: ...ns
```

## Project Structure
- **`cappuccino.scanner`**: Tokenizes the input string.
- **`cappuccino.parser`**: Parses tokens into an Abstract Syntax Tree (AST).
- **`cappuccino.nodes`**: Defines the AST nodes.
- **`cappuccino.interpreter`**: Evaluates the AST.
- **`cappuccino.utils`**: Utility classes for type checking and error reporting.
