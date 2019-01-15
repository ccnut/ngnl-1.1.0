
package com.ngnl.core.utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.ngnl.core.annotations.Nullable;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Rick Evans
 * @author Arjen Poutsma
 * @author Sam Brannen
 * @author Brian Clozel
 * @since 16 April 2001
 */
public abstract class StringUtils {

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	
	
	//---------------------------------------------------------------------
	// 字符串的通用便利方法
	//---------------------------------------------------------------------

	/**
	 * 检查 {@code String} 是否为空.
	 * <p>方法可接受任意Object类型参数, 并与 {@code null} 和空字符串""做对比. 
	 * <p>若传如的Object参数non-String并且non-null, 将总是返回false.
	 */
	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || "".equals(str));
	}

	/**
	 * 检查 {@code CharSequence} 不为 {@code null} 并且 length>0
	 * <p>Note: 如果 {@code CharSequence} 只包含空格,也将返回 {@code true}.
	 * <p><pre class="code">
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str  {@code CharSequence} (可为 {@code null})
	 * @return {@code true} 如果 {@code CharSequence} 不为 {@code null} 并且 length>0
	 * @see #hasText(String)
	 */
	public static boolean hasLength(@Nullable CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 检查 {@code String} 不为 {@code null} 并 length > 0
	 * <p>Note: 如果 {@code String} 只包含空格,也将返回 {@code true}.
	 * @param str {@code String}  (可为 {@code null})
	 * @return {@code true} 如果 {@code String} 不为 {@code null} 并且 length>0
	 * @see #hasLength(CharSequence)
	 * @see #hasText(String)
	 */
	public static boolean hasLength(@Nullable String str) {
		return (str != null && !str.isEmpty());
	}

	/**
	 * 检查 {@code CharSequence} 是否包含有效的文本内容 <em>text</em>.
	 * <p>具体解释: returns {@code true} 时,表示 {@code CharSequence} 必不为 {@code null}, 长度 length>0 并至少包含一个 non-whitespace 字符.
	 * <p><pre class="code">
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * @param str {@code CharSequence}  (may be {@code null})
	 * @return {@code true} 如果 {@code CharSequence} 不为 {@code null}, 长度 length > 0, 并且不只包含空格.
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(@Nullable CharSequence str) {
		return (str != null && str.length() > 0 && containsText(str));
	}

	/**
	 * 检查 {@code String} 是否包含有效的文本内容 <em>text</em>.
	 * <p>具体解释: returns {@code true} 时,表示 {@code String} 必不为 {@code null}, 长度 length>0 并至少包含一个 non-whitespace 字符.
	 * @param str {@code String}  (may be {@code null})
	 * @return {@code true} 如果 {@code String} 不为 {@code null}, 长度 length > 0, 并且不只包含空格.
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(@Nullable String str) {
		return (str != null && !str.isEmpty() && containsText(str));
	}

	private static boolean containsText(CharSequence str) {
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查 {@code CharSequence} 是否包含whitespace character.
	 * @param str {@code CharSequence}  (may be {@code null})
	 * @return {@code true} 如果 {@code CharSequence} 非空字符串,并且至少包含一个 whitespace character.
	 * @see Character#isWhitespace
	 */
	public static boolean containsWhitespace(@Nullable CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}

		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查 {@code String} 是否包含whitespace character.
	 * @param str {@code String}  (may be {@code null})
	 * @return {@code true} 如果 {@code String} 非空字符串,并且至少包含一个whitespace character.
	 * @see #containsWhitespace(CharSequence)
	 */
	public static boolean containsWhitespace(@Nullable String str) {
		return containsWhitespace((CharSequence) str);
	}

	/**
	 * 去除 {@code String} 的 <b>首尾whitespace character</b>.
	 * @param str  {@code String}
	 * @return 已去除<b>首尾空格</b>的 {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}

		int beginIndex = 0;
		int endIndex = str.length() - 1;

		while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
			beginIndex++;
		}

		while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
			endIndex--;
		}

		return str.substring(beginIndex, endIndex + 1);
	}

	/**
	 * 去除 {@code String} 中的<b>所有whitespace character</b>.
	 * <p>包括: <b>首尾</b> 以及 <b>字符中间</b> 的所有whitespace character.
	 * @param str  {@code String}
	 * @return 已去除所有whitespace character的 {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}

		int len = str.length();
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 去除 {@code String} 行首部分出现的<i>所有</i>whitespace character
	 * @param str {@code String}
	 * @return 已去除whitespace character的 {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	/**
	 * 去除 {@code String} 行尾部分出现的<i>所有</i>whitespace character
	 * @param str {@code String} 
	 * @return 已去除whitespace character的 {@code String}
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 去除 {@code String} 行首部分出现的<i>所有</i>的指定字符 {@code leadingCharacter}
	 * @param str  {@code String}
	 * @param leadingCharacter 要删除的目标字符
	 * @return the trimmed {@code String}
	 */
	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	/**
	 * 去除 {@code String} 行尾部分出现的<i>所有</i>的指定字符 {@code leadingCharacter}
	 * @param str  {@code String}
	 * @param trailingCharacter 要删除的目标字符
	 * @return the trimmed {@code String}
	 */
	public static String trimTrailingCharacter(String str, char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 测试给定 {@code String} 是否以给定的prefix开头.
	 * <p> 忽略大小写
	 * @param str 待检查 {@code String} 
	 * @param prefix 给定的开头字符串
	 * @see java.lang.String#startsWith
	 */
	public static boolean startsWithIgnoreCase(@Nullable String str, @Nullable String prefix) {
		return (str != null && prefix != null 
					&& str.length() >= prefix.length() 
						&& str.regionMatches(true, 0, prefix, 0, prefix.length()));
	}

	/**
	 * 测试给定 {@code String} 是否以给定的suffix开结尾.
	 * <p> 忽略大小写
	 * @param str 待检查 {@code String} 
	 * @param suffix 给定的结尾字符串
	 * @see java.lang.String#endsWith
	 */
	public static boolean endsWithIgnoreCase(@Nullable String str, @Nullable String suffix) {
		return (str != null && suffix != null && str.length() >= suffix.length() &&
				str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
	}

	/**
	 * 测试 {@code CharSequence str} 从第index开始匹配给定的 {@code CharSequence substring}
	 * @param str 原始字符串 (or StringBuilder)
	 * @param index 在原始字符串中开始匹配 {@code CharSequence substring} 的index
	 * @param substring 测试匹配的目标字符串
	 */
	public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
		if (index + substring.length() > str.length()) {
			return false;
		}
		for (int i = 0; i < substring.length(); i++) {
			if (str.charAt(index + i) != substring.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 计数 {@code String sub} 在  {@code String str} 中出现的次数
	 * @param str 待检索的原始字符串
	 * @param sub 匹配目标字符串
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (!hasLength(str) || !hasLength(sub)) {
			return 0;
		}

		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * 替换字符串{@code String inString}中出现的<i>所有</i>{@code String oldPattern} 为 {@code String newPattern}
	 * @param inString {@code String} 原始字符串
	 * @param oldPattern {@code String} 被换字符串
	 * @param newPattern {@code String} 替代字符串
	 * @return  包含新字符串的{@code String} 
	 */
	public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		int index = inString.indexOf(oldPattern);
		if (index == -1) {
			// no occurrence -> can return input as-is
			return inString;
		}

		int capacity = inString.length();
		if (newPattern.length() > oldPattern.length()) {
			capacity += 16;
		}
		StringBuilder sb = new StringBuilder(capacity);

		int pos = 0;  // our position in the old string
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}

		// append any characters to the right of a match
		sb.append(inString.substring(pos));
		return sb.toString();
	}

	/**
	 * 删除字符串中出现的<i>所有</i> {@code String pattern}.
	 * @param inString 原始 {@code String}
	 * @param pattern 待删除的{@code String}
	 * @return the resulting {@code String}
	 */
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	/**
	 * 删除 {@code String inString} 出现的<i>所有</i>出现在字符集合 {@code String charsToDelete} 中的字符
	 * @param inString 原始 {@code String}
	 * @param charsToDelete 待删除的字符集合
	 * 	   例如: "az\n" 表示删除<i>所有</i> 'a', <i>所有</i>'z' 和换行符.
	 * @return 新的 {@code String}
	 */
	public static String deleteAny(String inString, @Nullable String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}

		StringBuilder sb = new StringBuilder(inString.length());
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}


	//---------------------------------------------------------------------
	// 格式化字符串的方法
	//---------------------------------------------------------------------

	/**
	 * 对 {@code String str} 添加单引号.
	 * @param str 原始字符串 {@code String} (e.g. "myString")
	 * @return 添加单引号后的 {@code String} (e.g. "'myString'"),
	 * 	<p> <b>或</b>  {@code null} 如果传入 {@code String str == null}
	 */
	@Nullable
	public static String quote(@Nullable String str) {
		return (str != null ? "'" + str + "'" : null);
	}

	/**
	 * 对 {@code Object obj} 添加单引号.
	 * <p>如果 {@code Object obj} 是 {@code String}类型, 则添加单引号并返回; 
	 * <p>如果 {@code Object obj} 不是 {@code String}类型, 则原样返回.
	 * @param obj the input Object (e.g. "myString")
	 * @return the quoted {@code String} (e.g. "'myString'"),
	 * or the input object as-is if not a {@code String}
	 */
	@Nullable
	public static Object quoteIfString(@Nullable Object obj) {
		return (obj instanceof String ? quote((String) obj) : obj);
	}

	/**
	 * 删除 {@code String qualifiedName} 以 '.' 作为前置限定字符的前置限定字符串.
	 * <p>例如: "this.name.is.qualified", 将返回 "qualified".
	 * @param qualifiedName 待删除前置限定的字符串
	 */
	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	/**
	 * 删除 {@code String qualifiedName} 以 {@code char separator} 作为前置限定字符的前置限定字符串.
	 * <p>例如: 如果传入 ':' 作为限定分隔符, "this:name:is:qualified", 将返回 "qualified".
	 * @param qualifiedName 待删除限定的字符串
	 * @param separator 限定分隔符
	 */
	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	/**
	 * 大写 {@code String str} 首字母. 对首字母做 {@link Character#toUpperCase(char)} 操作.
	 * <p> 除首字母外, 其他字母不做改动.
	 * @param str 原始 {@code String}
	 * @return 修改后 {@code String}
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * 小写 {@code String str} 首字母. 对首字母做 {@link Character#toLowerCase(char)} 操作.
	 * <p> 除首字母外, 其他字母不做改动.
	 * @param str 原始 {@code String}
	 * @return 修改后 {@code String}
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (!hasLength(str)) {
			return str;
		}

		char baseChar = str.charAt(0);
		char updatedChar;
		if (capitalize) {
			updatedChar = Character.toUpperCase(baseChar);
		}
		else {
			updatedChar = Character.toLowerCase(baseChar);
		}
		if (baseChar == updatedChar) {
			return str;
		}

		char[] chars = str.toCharArray();
		chars[0] = updatedChar;
		return new String(chars, 0, chars.length);
	}

	/**
	 * 从 <i> {@code String path} </i> 中提取文件名.
	 * <p>举例: {@code "mypath/myfile.txt" -> "myfile.txt"}.
	 * @param path 一个文件路径 (可能 {@code null})
	 * @return 提取出的文件名 或 {@code null} ({@code String path == null})
	 */
	@Nullable
	public static String getFilename(@Nullable String path) {
		if (path == null) {
			return null;
		}

		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/**
	 * 从 {@code String path} 中提取文件扩展名.
	 * <p>举例: "mypath/myfile.txt" -> "txt".
	 * @param path 一个文件路径 (可能 {@code null})
	 * @return 提取出的文件扩展名 或 {@code null} ({@code String path == null})
	 */
	@Nullable
	public static String getFilenameExtension(@Nullable String path) {
		if (path == null) {
			return null;
		}

		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return null;
		}

		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return null;
		}

		return path.substring(extIndex + 1);
	}

	/**
	 * 从传入的文件路径  {@code String path} 中剥掉文件扩展名
	 * 举例: "mypath/myfile.txt" -> "mypath/myfile".
	 * @param path 文件路径
	 * @return 去除文件扩展名后的字符串
	 */
	public static String stripFilenameExtension(String path) {
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return path;
		}

		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return path;
		}

		return path.substring(0, extIndex);
	}

	/**
	 * 将资源路径 {@code String path} 和相对路径{@code String relativePath}拼接起来.(默认使用标准java分隔符 "/")
	 * <p>举例: "d:/ngnl/" + "mypath/myfile.txt" -> "d:/ngnl/mypath/myfile.txt".
	 * @param path 路径的前半部分 (通常为一个文件路径)
	 * @param relativePath 拼接的后半部分相对路径
	 * @return 拼接后的完整路径
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		}
		else {
			return relativePath;
		}
	}

	/**
	 * 返回一个标准化的等效路径. 合并整理例如 "path/.." "./" "../" "\\"),
	 * <p> 返回值方便清晰的比对路径. 如用于其他用途,请注意Windows中的分隔符"\"将被"/"替代
	 * <p><pre class="code">
	 * assertEquals("mypath/myfile", StringUtils.cleanPath("mypath/myfile"));
	 * assertEquals("mypath/myfile", StringUtils.cleanPath("mypath\\myfile"));
	 * assertEquals("mypath/myfile", StringUtils.cleanPath("mypath/../mypath/myfile"));
	 * assertEquals("mypath/myfile", StringUtils.cleanPath("mypath/myfile/../../mypath/myfile"));
	 * assertEquals("../mypath/myfile", StringUtils.cleanPath("../mypath/myfile"));
	 * assertEquals("../mypath/myfile", StringUtils.cleanPath("../mypath/../mypath/myfile"));
	 * assertEquals("../mypath/myfile", StringUtils.cleanPath("mypath/../../mypath/myfile"));
	 * assertEquals("/../mypath/myfile", StringUtils.cleanPath("/../mypath/myfile"));
	 * assertEquals("/mypath/myfile", StringUtils.cleanPath("/a/:b/../../mypath/myfile"));
	 * assertEquals("file:///c:/path/to/the%20file.txt", StringUtils.cleanPath("file:///c:/some/../path/to/the%20file.txt"));
	 * </pre>
	 * 
	 * @param path 原始路径
	 * @return 标准化后的路径
	 */
	public static String cleanPath(String path) {
		if (!hasLength(path)) {
			return path;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(':');
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (prefix.contains("/")) {
				prefix = "";
			}
			else {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			}
			else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			}
			else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				}
				else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * 标准化两个path, 并判断二者是否相同{@code string.equals(str)}
	 * <p><pre class="code">
	 * assertTrue("Must be true for current paths",
	 * 	StringUtils.pathEquals("./dummy1/dummy2/dummy3", "dummy1/dum/./dum/../../dummy2/dummy3"));
	 * assertFalse("Must be false for relative/absolute paths", 
	 * 	StringUtils.pathEquals("./dummy1/dummy2/dummy3", "/dummy1/dum/./dum/../../dummy2/dummy3"));
	 * assertFalse("Must be false for different strings", 
	 * 	StringUtils.pathEquals("/dummy1/dummy2/dummy3", "/dummy1/dummy4/dummy3"));
	 * </pre>
	 * @param path1 第一个路径
	 * @param path2 第二个路径
	 * @return 标准化整理后的两个路径是否相同
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * 解码{@code String source}. 基于以下规则:
	 * <ul>
	 * <li>数字和字母字符 {@code "a"} 到 {@code "z"}, {@code "A"} 到 {@code "Z"}, {@code "0"} 到 {@code "9"} 保持不变.</li>
	 * <li>特殊字符 {@code "-"}, {@code "_"}, {@code "."}, {@code "*"} 保持不变.</li>
	 * <li>序列"{@code %<i>xy</i>}" 将被解释为对应字符的16进制.</li>
	 * </ul>
	 * @param source 编码前字符串
	 * @param charset 字符集
	 * @return 解码后的字符串
	 * @throws IllegalArgumentException 当给定源包含无效编码序列时
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public static String uriDecode(String source, Charset charset) {
		int length = source.length();
		if (length == 0) {
			return source;
		}
		Assert.notNull(charset, "Charset must not be null");

		ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			int ch = source.charAt(i);
			if (ch == '%') {
				if (i + 2 < length) {
					char hex1 = source.charAt(i + 1);
					char hex2 = source.charAt(i + 2);
					int u = Character.digit(hex1, 16);
					int l = Character.digit(hex2, 16);
					if (u == -1 || l == -1) {
						throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
					}
					bos.write((char) ((u << 4) + l));
					i += 2;
					changed = true;
				}
				else {
					throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
				}
			}
			else {
				bos.write(ch);
			}
		}
		return (changed ? new String(bos.toByteArray(), charset) : source);
	}

	/**
	 * 解析传入的 {@code String localeValue} 成一个 {@link Locale} 对象,
	 * 接受  {@link Locale#toString} 格式的字符串以及 BCP 47 语言标记.
	 * @param localeValue the locale value: following either {@code Locale's}
	 * {@code toString()} format ("en", "en_UK", etc), also accepting spaces as
	 * separators (as an alternative to underscores), or BCP 47 (e.g. "en-UK")
	 * as specified by {@link Locale#forLanguageTag} on Java 7+
	 * @return a corresponding {@code Locale} instance, or {@code null} if none
	 * @throws IllegalArgumentException in case of an invalid locale specification
	 * @since 5.0.4
	 * @see #parseLocaleString
	 * @see Locale#forLanguageTag
	 */
	@Nullable
	public static Locale parseLocale(String localeValue) {
		String[] tokens = tokenizeLocaleSource(localeValue);
		if (tokens.length == 1) {
			return Locale.forLanguageTag(localeValue);
		}
		return parseLocaleTokens(localeValue, tokens);
	}

	/**
	 * 将  {@code String localeString} 表述解析成为一个 {@link Locale} 实例.
	 * <p>这个操作是 {@link Locale#toString} 的逆操作.
	 * @param localeString the locale {@code String}: following {@code Locale's}
	 * {@code toString()} format ("en", "en_UK", etc), also accepting spaces as
	 * separators (as an alternative to underscores)
	 * <p>Note: This variant does not accept the BCP 47 language tag format.
	 * Please use {@link #parseLocale} for lenient parsing of both formats.
	 * @return a corresponding {@code Locale} instance, or {@code null} if none
	 * @throws IllegalArgumentException in case of an invalid locale specification
	 */
	@Nullable
	public static Locale parseLocaleString(String localeString) {
		return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
	}

	private static String[] tokenizeLocaleSource(String localeSource) {
		return tokenizeToStringArray(localeSource, "_ ", false, false);
	}

	@Nullable
	private static Locale parseLocaleTokens(String localeString, String[] tokens) {
		String language = (tokens.length > 0 ? tokens[0] : "");
		String country = (tokens.length > 1 ? tokens[1] : "");
		validateLocalePart(language);
		validateLocalePart(country);

		String variant = "";
		if (tokens.length > 2) {
			// There is definitely a variant, and it is everything after the country
			// code sans the separator between the country code and the variant.
			int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
			// Strip off any leading '_' and whitespace, what's left is the variant.
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return (language.length() > 0 ? new Locale(language, country, variant) : null);
	}

	private static void validateLocalePart(String localePart) {
		for (int i = 0; i < localePart.length(); i++) {
			char ch = localePart.charAt(i);
			if (ch != ' ' && ch != '_' && ch != '#' && !Character.isLetterOrDigit(ch)) {
				throw new IllegalArgumentException(
						"Locale part \"" + localePart + "\" contains invalid characters");
			}
		}
	}

	/**
	 * Determine the RFC 3066 compliant language tag,
	 * as used for the HTTP "Accept-Language" header.
	 * @param locale the Locale to transform to a language tag
	 * @return the RFC 3066 compliant language tag as {@code String}
	 * @deprecated as of 5.0.4, in favor of {@link Locale#toLanguageTag()}
	 */
	@Deprecated
	public static String toLanguageTag(Locale locale) {
		return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
	}

	/**
	 * 解析 {@code timeZoneString} 成一个 {@link TimeZone} 对象.
	 * @param timeZoneString 一个timeZone字符串, 需遵循 {@link TimeZone#getTimeZone(String)} 格式.
	 * @return 一个等效的 {@link TimeZone} 实例.
	 * @throws IllegalArgumentException 无效的时区描述时.
	 */
	public static TimeZone parseTimeZoneString(String timeZoneString) {
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
		if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
			// We don't want that GMT fallback...
			throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
		}
		return timeZone;
	}


	//---------------------------------------------------------------------
	// 处理String arrays相关的通用方法
	//---------------------------------------------------------------------

	/**
	 * 将传入的{@code String} 与 {@code String} 数组合并, 返回一个新的数组实例;
	 * <p>{@code String} 位于新数组的末尾.
	 * @param array String[] (可以传入 {@code null})
	 * @param str {@code String}
	 * @return 一个新的String[] (永不返回 {@code null})
	 */
	public static String[] addStringToArray(@Nullable String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] {str};
		}

		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	/**
	 * 将指定的两个 {@code String} 数组合并为一个, 新的数组包含这两个数组的所有元素
	 * <p>数组内元素的顺序会被保留.
	 * @param array1 第一个数组 (可以为 {@code null})
	 * @param array2 第二个数组 (可以为 {@code null})
	 * @return 新的数组. 
	 * <p>如果array1和array2都为{@code null}, 则返回的新数组也为{@code null}
	 */
	@Nullable
	public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}

		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	/**
	 * 合并两个{@code String}数组.
	 * Merge the given {@code String} arrays into one, with overlapping
	 * array elements only included once.
	 * <p>数组内元素的顺序会被保留.
	 * (with the exception of overlapping elements, which are only
	 * included on their first occurrence).
	 * @param array1 the first array (can be {@code null})
	 * @param array2 the second array (can be {@code null})
	 * @return the new array ({@code null} if both given arrays were {@code null})
	 * @deprecated as of 4.3.15, in favor of manual merging via {@link LinkedHashSet}
	 * (with every entry included at most once, even entries within the first array)
	 */
	@Deprecated
	@Nullable
	public static String[] mergeStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}

		List<String> result = new ArrayList<>();
		result.addAll(Arrays.asList(array1));
		for (String str : array2) {
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	/**
	 * 对 {@code String}数组中的元素进行一次排序.
	 * @param array 数据源
	 * @return 排序后的数组(永远不为 {@code null})
	 */
	public static String[] sortStringArray(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}

		Arrays.sort(array);
		return array;
	}

	/**
	 * 将 {@code Collection} 转化为一个 {@code String} 数组.
	 * <p>{@code Collection} 中的元素必须全部是 {@code String} 类型.
	 * @param collection {@code Collection}对象
	 * @return 包含 {@code Collection} 全部元素的 {@code String} 数组
	 */
	public static String[] toStringArray(Collection<String> collection) {
		return collection.toArray(new String[0]);
	}

	/**
	 * 将一个 Enumeration 转换为 {@code String} 数组.
	 * <p> Enumeration 对象必须只包含 {@code String} 元素.
	 * @param enumeration 待转换的 Enumeration 对象
	 * @return 转换后 {@code String} 数组
	 */
	public static String[] toStringArray(Enumeration<String> enumeration) {
		return toStringArray(Collections.list(enumeration));
	}

	/**
	 * 对 {@code String} 数组中的每个对象做 {@code String.trim()} 操作.
	 * @param array 原始 {@code String} 数组
	 * @return {@code String.trim()} 操作后的数组 (与原数组长度一致).
	 */
	public static String[] trimArrayElements(@Nullable String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}

		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? element.trim() : null);
		}
		return result;
	}

	/**
	 * 去除{@code String}数组中相同的string.
	 * <p>As of 4.2, it preserves the original order, as it uses a {@link LinkedHashSet}.
	 * @param array the {@code String} array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}

		Set<String> set = new LinkedHashSet<>();
		for (String element : array) {
			set.add(element);
		}
		return toStringArray(set);
	}

	/**
	 * 从出现的第一个 {@code delimiter} 处, 截断指定的 {@code String}, 并返回一个 {@code String}数组.
	 * <p> 阶段后的两个string不包含 {@code delimiter}.
	 * @param toSplit 待截断的{@code String}
	 * @param delimiter 分隔符
	 * @return 分隔符前后的两段字符串, 或未找到分隔符时返回{@code null}
	 */
	@Nullable
	public static String[] split(@Nullable String toSplit, @Nullable String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}

		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] {beforeDelimiter, afterDelimiter};
	}

	/**
	 * 对 {@code array} 中的每个 string 按{@code delimiter} 进行截断.
	 * {@code delimiter} 左边的作为 {@code Properties} 的key, 右边的作为 {@code Properties} 的value, 
	 * <p>会对截断后的string进行{@code String#trim()}操作后, 再作为key 和 value 来实例 {@code Properties} 对象.
	 * @param array 待转换的string数组
	 * @param delimiter 分隔符 (常用 = 号)
	 * @return 一个可以表示数组内容的 {@code Properties} 实例, 或 null ({@code array 参数为null或元素长度为0}).
	 */
	@Nullable
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	/**
	 * 对 {@code array} 中的每个 string 按{@code delimiter} 进行截断.
	 * {@code delimiter} 左边的作为 {@code Properties} 的key, 右边的作为 {@code Properties} 的value, 
	 * <p>会对截断后的string进行{@code String#trim()}操作后, 再作为key 和 value 来实例 {@code Properties} 对象.
	 * @param array 待转换的string数组
	 * @param delimiter 分隔符 (常用 = 号)
	 * @param charsToDelete 在尝试拆分操作（通常是引号符号）之前，从每个元素中移除一个或多个字符，或者如果不进行删除，则传入{@code null}。
	 * @return 一个可以表示数组内容的 {@code Properties} 实例, 或 null ({@code array 参数为null或元素长度为0}).
	 */
	@Nullable
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, @Nullable String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}

		Properties result = new Properties();
		for (String element : array) {
			if (charsToDelete != null) {
				element = deleteAny(element, charsToDelete);
			}
			String[] splittedElement = split(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	/**
	 * 使用 {@link StringTokenizer} 分词类, 将指定的 {@code String} 分割为一个 {@code String}数组. 
	 * <p>会对分割后的子字符串做{@code String#trim()}. 如果子字符串为空字符串串, 则会省略掉这个空字符串.
	 * <p>{@code delimiters} 参数可以是任意数量的分割字符(char). 将使用每个分割字符(char)对 {@code String} 进行分割. 
	 * <p>如果希望使用 {@code delimiters} 作为一个字符串整体, 对{@code String} 进行分割, 使用 {@link #delimitedListToStringArray}.
	 * @param str 带切分的{@code String}
	 * @param delimiters 单个或多个分割字符组成的 {@code String}
	 * (每个字符分别被视为分割符。)
	 * @return 分割后的{@code String}数组
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * 使用 {@link StringTokenizer} 分词类, 将指定的 {@code String} 分割为一个 {@code String}数组. 
	 * <p>会对分割后的子字符串做{@code String#trim()}. 如果子字符串为空字符串串, 则会省略掉这个空字符串.
	 * <p>{@code delimiters} 参数可以是任意数量的分割字符(char). 将使用每个分割字符(char)对 {@code String} 进行分割. 
	 * <p>如果希望使用 {@code delimiters} 作为一个字符串整体, 对{@code String} 进行分割, 使用 {@link #delimitedListToStringArray}.
	 * @param str 带切分的{@code String}
	 * @param delimiters 单个或多个分割字符组成的 {@code String}
	 * @param trimTokens 是否对每个子字符串 {@link String#trim()}
	 * @param ignoreEmptyTokens 是否忽略为空的子字符串(对子字符串{@code String#trim()} 操作后).
	 * @return 分割后的{@code String}数组
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(@Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return new String[0];
		}

		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * 将{@code str} 用字符串{@code delimiter} 分割成一个 {@code String}数组.
	 * <p> {@code delimiter} 可能包含有多个character, 区别于{@link #tokenizeToStringArray}的是, 
	 * 不会单独用每个字符来分割{@code String}, 而是将 {@code delimiter} 作为一个整体.
	 * @param str 待分割的 {@code String}
	 * @param delimiter 分隔符(作为一个整体来当做分隔符)
	 * @return 分割好的子字符串{@code String}数组
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * 将{@code str} 用字符串{@code delimiter} 分割成一个 {@code String}数组.
	 * <p> {@code delimiter} 可能包含有多个character, 区别于{@link #tokenizeToStringArray}的是, 
	 * 不会单独用每个字符来分割{@code String}, 而是将 {@code delimiter} 作为一个整体.
	 * @param str 待分割的 {@code String}
	 * @param delimiter 分隔符(作为一个整体来当做分隔符)
	 * @param charsToDelete 一组要删除的字符; 通常要删除不需要的换行符时很实用. e.g. "\r\n\f" will delete all new lines and line feeds in a {@code String}
	 * @return 分割好的子字符串{@code String}数组
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {

		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] {str};
		}

		List<String> result = new ArrayList<>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		}
		else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	/**
	 * 将一个逗号分割的{@code str} (e.g., a row from a CSV file) 转换为一个 {@code String[]}
	 * @param str 输入的 {@code String}
	 * @return 一个{@code String[]} 或者 长度为0的{@code String[]} (如果{@code str}为空)
	 */
	public static String[] commaDelimitedListToStringArray(@Nullable String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * 将一个逗号分割的{@code str} (e.g., a row from a CSV file) 转换为一个 {@code Set}
	 * <p>注意，这将删除重复(Set中不能添加重复元素)，并且在4.2中，返回的集合中的元素将保留原始顺序。{@link LinkedHashSet}.
	 * @param str 输入的 {@code String}
	 * @return 列表中的一组{@code String}条目
	 * @see #removeDuplicateStrings(String[])
	 */
	public static Set<String> commaDelimitedListToSet(@Nullable String str) {
		Set<String> set = new LinkedHashSet<>();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (String token : tokens) {
			set.add(token);
		}
		return set;
	}

	/**
	 * 将 {@link Collection} 转换为由分隔符分割的 {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param coll the {@code Collection} to convert
	 * @param delim the delimiter to use (typically a ",")
	 * @param prefix the {@code String} to start each element with
	 * @param suffix the {@code String} to end each element with
	 * @return the delimited {@code String}
	 */
	public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim, String prefix, String suffix) {

		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param coll the {@code Collection} to convert
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param coll the {@code Collection} to convert
	 * @return the delimited {@code String}
	 */
	public static String collectionToCommaDelimitedString(Collection<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param arr the array to display
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		if (arr.length == 1) {
			return ObjectUtils.nullSafeToString(arr[0]);
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * Convert a {@code String} array into a comma delimited {@code String}
	 * (i.e., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 * @param arr the array to display
	 * @return the delimited {@code String}
	 */
	public static String arrayToCommaDelimitedString(@Nullable Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

}
