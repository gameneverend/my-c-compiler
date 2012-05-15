#!/bin/sh

rm seperate_tests.sh
touch seperate_tests.sh

echo "#!/bin/sh" >> seperate_tests.sh
echo "ant clean" >> seperate_tests.sh
echo "ant" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "OUR_COMPILER=\"java -jar jar/Compiler.jar\"" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "rm full" >> seperate_tests.sh
echo >> seperate_tests.sh

i=0
END=.c
for entry in $(ls tests/singular_tests/*$END)
do
  substring=${entry%$END}
  echo "TEST$i="$substring >> seperate_tests.sh
  echo >> seperate_tests.sh
  files[$i]="TEST$i"
  i=$(($i + 1))
  
#  for prototypes in `grep 
done

echo >> seperate_tests.sh

i=0
for compile in ${files[@]}
do
  echo "\$OUR_COMPILER \$$compile"$END >> seperate_tests.sh
done

echo >> seperate_tests.sh

echo -n gcc -g" " >> seperate_tests.sh
for gcc in ${files[@]}
do
  echo -n \$$gcc.s" " >> seperate_tests.sh
done
echo -n tests/full_test.c -o full >> seperate_tests.sh

echo >> seperate_tests.sh

echo ./full >> seperate_tests.sh
