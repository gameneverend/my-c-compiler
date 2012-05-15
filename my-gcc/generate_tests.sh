#!/bin/sh

rm seperate_tests.sh
rm tests/batterie_test.h
touch seperate_tests.sh
touch tests/batterie_test.h

echo "#!/bin/sh" >> seperate_tests.sh
echo "ant clean" >> seperate_tests.sh
echo "ant" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "OUR_COMPILER=\"java -jar jar/Compiler.jar\"" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "rm full" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "#ifndef BATTERIE_TEST"  >> tests/batterie_test.h
echo "#define BATTERIE_TEST" >> tests/batterie_test.h
echo >> tests/batterie_test.h
i=0
END=.c
for entry in $(ls tests/singular_tests/*$END)
do
  substring=${entry%$END}
  echo "TEST$i="$substring >> seperate_tests.sh
  files[$i]="TEST$i"
  i=$(($i + 1))
  str=""
  for prototypes in `grep '^\<int\>' $entry | grep ';$'`
  do
    if [[ "$prototypes" == *\; ]]
    then
      str="extern"$str" "$prototypes"\r"
      echo -e $str >> tests/batterie_test.h
      str=""
    else
      str=$str" "$prototypes
    fi
  done
  echo -n $str >> tests/batterie_test.h
  str=""
done

echo >> tests/batterie_test.h
echo "#endif" >> tests/batterie_test.h
echo >> seperate_tests.sh

i=0
for compile in ${files[@]}
do
  echo "if [ -e \$$compile.s ]" >> seperate_tests.sh
  echo "then" >> seperate_tests.sh
  echo "  rm \$$compile.s" >> seperate_tests.sh
  echo "fi" >> seperate_tests.sh
  echo "\$OUR_COMPILER \$$compile"$END >> seperate_tests.sh
  echo >> seperate_tests.sh
done

#echo "mv *.s tests/singular_tests/asm"

echo >> seperate_tests.sh

echo -n gcc -g" " >> seperate_tests.sh
for gcc in ${files[@]}
do
  echo -n \$$gcc.s" " >> seperate_tests.sh
done
echo -n tests/full_test.c -o full >> seperate_tests.sh

echo >> seperate_tests.sh

echo ./full >> seperate_tests.sh
