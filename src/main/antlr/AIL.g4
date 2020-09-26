grammar AIL;

@header {
package app.avalia.antlr;
}

parse
    : base EOF
    ;

base
    : (declaration | instruction)+
    ;

declaration
    : DECLARE name LBRACK instruction* RBRACK
    ;

instruction
    : name (DOLLAR_SIGN id)? (LPAREN arguments RPAREN)?
    (LBRACK instruction* RBRACK)?
    ;

name
    : IDENTIFIER;

id
    : NUMBER_LITERAL;

arguments
    : argument (COMMA argument)*
    ;

argument
    : type
    | delegate
    | type? value
    ;

value
    : TEXT_LITERAL | NUMBER_LITERAL | DECIMAL_LITERAL | NULL_LITERAL | BOOL_LITERAL
    ;

delegate
    : DELEGATE name
    ;

type
    : INT | CHAR
    | SHORT | BOOL
    | REF | BYTE
    | TEXT | DOUBLE
    | FLOAT | LONG
    ;

// Operators
DOLLAR_SIGN: '$';
LBRACK: '{';
RBRACK: '}';
LPAREN: '(';
RPAREN: ')';
COMMA: ',';

// Keywords
DELEGATE: 'delegate';
DECLARE: 'decl';

// Types
INT: 'int';
CHAR: 'char';
SHORT: 'short';
BOOL: 'bool';
REF: 'ref';
BYTE: 'byte';
TEXT: 'text';
DOUBLE: 'double';
FLOAT: 'float';
LONG: 'long';

// Literals
TEXT_LITERAL
    : ["] ( ~["\r\n\\] | '\\' ~[\r\n] )* ["]
    | ['] ( ~['\r\n\\] | '\\' ~[\r\n] )* [']
    ;
NUMBER_LITERAL: [-]? [0-9]+;
DECIMAL_LITERAL: [-]? [0-9]+ ([.] [0-9]+)?;
BOOL_LITERAL: 'true' | 'false';

NULL_LITERAL: 'null';

IDENTIFIER: [a-zA-Z]+;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;
WS
    : [ \t\u000C\r\n]+ -> skip
    ;
