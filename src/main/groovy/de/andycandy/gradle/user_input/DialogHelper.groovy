package de.andycandy.gradle.user_input

import javax.swing.*
import javax.swing.filechooser.FileFilter
import java.awt.event.WindowFocusListener

@Singleton
public class DialogHelper implements IDialogHelper {
	
	private static final Integer OK_OPTION = Integer.valueOf(JOptionPane.OK_OPTION)

	@Override
	public void createAndShowInputDialog(JComponent component, String title, String message) throws CancelException {
		
		List messages = []
		if (message != null) {
			messages << message
		}
		messages << component
		
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage(messages as Object[]);
		optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
		optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog = optionPane.createDialog(null, title);
		WindowFocusListener listener = createWindowFocusListener {
			component.requestFocus()
			dialog.removeWindowFocusListener(self as WindowFocusListener)
		}
		dialog.addWindowFocusListener(listener)
		
		dialog.setVisible(true)
		
		if (!isOKOption(optionPane.getValue())) throw new CancelException()
	}

	@Override
	boolean createYesNoDialog(String title, String message) throws CancelException {

		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage([message] as Object[]);
		optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
		optionPane.setOptionType(JOptionPane.YES_NO_OPTION);
		JDialog dialog = optionPane.createDialog(null, title);

		dialog.setVisible(true)
		Object value = optionPane.getValue()

		if (value instanceof Integer) {
			if (value == JOptionPane.YES_OPTION) {
				return true
			}
			if (value == JOptionPane.NO_OPTION) {
				return false
			}
		}

		throw new CancelException()
	}

	@Override
	File createAndShowFileChooserDialog(String title, Integer fileSelectionMode, Integer dialogType, List extensions, File currentDirectory) throws CancelException {

		JFileChooser dialog = new JFileChooser (
				dialogTitle: title,
				fileSelectionMode: fileSelectionMode,
				dialogType: dialogType,
				currentDirectory: currentDirectory)

		extensions.each { extension ->
			Closure acceptClosure = { File file ->
				file.directory || '*' == extension || file.name.endsWith(".$extension")
			}
			dialog.addChoosableFileFilter([getDescription: {-> ".$extension".toString()}, accept: acceptClosure] as FileFilter)
		}

		if (dialog.showDialog(null, null) != JFileChooser.APPROVE_OPTION) {
			throw new CancelException()
		}

		return dialog.selectedFile
	}

	private boolean isOKOption(Object option) {
		return OK_OPTION == option
	}
	
	private WindowFocusListener createWindowFocusListener(@DelegatesTo(SelfInstanceProvider) Closure closure) {
		
		Object delegate = new Object()
		delegate.metaClass.getSelf = { -> return closure }
		
		closure.delegate = delegate as SelfInstanceProvider
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		
		return closure as WindowFocusListener
	}

	interface SelfInstanceProvider {
		Closure getSelf()
	}
}
